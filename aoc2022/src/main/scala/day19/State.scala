package day19

case class State(robots: Vector[Int], resources: Vector[Int]) {
  def step(blueprint: Vector[Vector[Int]]): Seq[State] = {
    Seq(
      (Vector(0, 0, 0, 0), resources),
      (Vector(1, 0, 0, 0), (resources zip blueprint(0)).map(_ - _)),
      (Vector(0, 1, 0, 0), (resources zip blueprint(1)).map(_ - _)),
      (Vector(0, 0, 1, 0), (resources zip blueprint(2)).map(_ - _)),
      (Vector(0, 0, 0, 1), (resources zip blueprint(3)).map(_ - _)),
    )
      .filter(_._2.forall {_ >= 0})
      .map { (robotBuilds, resources) =>
        State((robots zip robotBuilds).map(_ + _), (resources zip robots).map(_ + _))
      }
  }
}
