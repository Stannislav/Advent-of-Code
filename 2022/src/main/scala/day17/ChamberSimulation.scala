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

  private var rockIdx = 0
  private var jetIdx = 0
  private var chamber: Array[Int] = Array()

  private def chamberHash(chamber: Array[Int]) = chamber.takeRight(30).toSeq.hashCode()

  def run(jets: String): (Array[Int], Int, Int) = {
    rockIdx = 0
    jetIdx = 0
    chamber = Array()
    // (jetIdx, rockIdx, chamberHash) => (nSteps, height)
    val cache = collection.mutable.Map[(Int, Int, Int), (Int, Int)]()
    var nSteps = 0

    // Simulate rock falling until a loop is detected.
    while (!cache.contains((jetIdx, rockIdx, chamberHash(chamber)))) {
      cache((jetIdx, rockIdx, chamberHash(chamber))) = (nSteps, chamber.length)
      dropRock(ROCK_SHAPES(rockIdx), jets)
      rockIdx = (rockIdx + 1) % ROCK_SHAPES.length
      nSteps += 1
    }

    // Translate cached chamber states into an array of heights indexed
    // by the step number. Also compute the loop length and the height gain
    // in the loop.
    val (loopStart, startHeight) = cache((jetIdx, rockIdx, chamberHash(chamber)))
    val heights = Array.fill(cache.size)(0)
    for ((idx, height) <- cache.values) {
      heights(idx) = height
    }
    val loopHeightGain = chamber.length - heights(loopStart)
    (heights, loopStart, loopHeightGain)
  }

  private def dropRock(rockShape: Seq[String], jets: String): Unit = {
    var pos = chamber.length + 3
    var resting = false
    val rock = Rock.fromShape(rockShape)
    while (!resting) {
      // 1. Shift left-right
      jets(jetIdx) match {
        case '<' => rock.maybeMoveLeft(chamber, pos)
        case '>' => rock.maybeMoveRight(chamber, pos)
      }
      jetIdx = (jetIdx + 1) % jets.length

      // 2. Shift down
      pos -= 1
      if (pos < 0 || rock.isCollision(chamber, pos)) {
        pos += 1
        resting = true
      }
    }
    chamber = rock.mergeIntoChamber(chamber, pos)
  }
}
