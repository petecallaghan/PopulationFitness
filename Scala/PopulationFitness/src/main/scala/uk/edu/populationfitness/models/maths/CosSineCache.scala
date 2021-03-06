package uk.edu.populationfitness.models.maths

object CosSineCache {
  private val _degrees = 360
  private val _entries = _degrees + 1
  private val _cos = Array.tabulate(_entries) {x => math.cos(math.toRadians(x))}
  private val _sin = Array.tabulate(_entries) {x => math.sin(math.toRadians(x))}

  private def lookup(angle: Double, values: Seq[Double]) : Double = {
    val angleCircle = angle.intValue() % _degrees
    if (angle < 0) 0 - values( 0 - angleCircle) else values(angleCircle)
  }

  def sin(angle: Double) : Double = lookup(angle, _sin)
  def cos(angle: Double) : Double = lookup(angle, _cos)
}
