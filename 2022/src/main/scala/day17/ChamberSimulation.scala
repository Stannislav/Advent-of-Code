package day17

object ChamberSimulation {
  private val ROCK_SHAPES: Seq[Seq[String]] = Seq(
    Seq("####"),
    Seq(".#.", "###", ".#."),
    Seq("..#", "..#", "###"),
    Seq("#", "#", "#", "#"),
    Seq("##", "##"),
  )
  private val FULL_LINE = 127 // 0b1111111

  private def chamberHash(chamber: Array[Int]) = chamber.takeRight(30).toSeq.hashCode()

  def run(jetData: String): (Array[Int], Int, Int) = {
    val jets = Jets(jetData)
    var rockIdx = 0
    var chamber = Array[Int]()
    // (jetIdx, rockIdx, chamberHash) => (nSteps, height)
    val cache = collection.mutable.Map[(Int, Int, Int), (Int, Int)]()
    var nSteps = 0

    // Simulate rock falling until a loop is detected.
    while (!cache.contains((jets.idx, rockIdx, chamberHash(chamber)))) {
      cache((jets.idx, rockIdx, chamberHash(chamber))) = (nSteps, chamber.length)
      chamber = dropRock(ROCK_SHAPES(rockIdx), chamber, jets)
      rockIdx = (rockIdx + 1) % ROCK_SHAPES.length
      nSteps += 1
    }

    // Translate cached chamber states into an array of heights indexed
    // by the step number. Also compute the loop length and the height gain
    // in the loop.
    val (loopStart, startHeight) = cache((jets.idx, rockIdx, chamberHash(chamber)))
    val heights = Array.fill(cache.size)(0)
    for ((idx, height) <- cache.values) {
      heights(idx) = height
    }
    val loopHeightGain = chamber.length - heights(loopStart)
    (heights, loopStart, loopHeightGain)
  }

  private def dropRock(rockShape: Seq[String], chamber: Array[Int], jets: Jets): Array[Int] = {
    var pos = chamber.length + 3
    var resting = false
    val rock = Rock.fromShape(rockShape)
    while (!resting) {
      // 1. Shift left-right
      jets.next() match {
        case '<' => rock.maybeMoveLeft(chamber, pos)
        case '>' => rock.maybeMoveRight(chamber, pos)
      }

      // 2. Shift down
      pos -= 1
      if (pos < 0 || rock.isCollision(chamber, pos)) {
        pos += 1
        resting = true
      }
    }
    rock.mergeIntoChamber(chamber, pos)
  }
}
