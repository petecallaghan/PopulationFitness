package uk.edu.populationfitness.models.fastmaths

object CosSineCache {
  private val _degrees = 360
  private val _entries = _degrees + 1
  private val _cos = Array.tabulate(_entries) {x => math.cos(math.toRadians(x))}
  private val _sin = Array.tabulate(_entries) {x => math.sin(math.toRadians(x))}

  private def lookup(angle: Double, values: Array[Double]) : Double = {
    val angleCircle = (angle % _degrees).intValue()
    if (angle < 0) 0 - values( 0 - angleCircle) else values(angleCircle)
  }

  def sin(angle: Double) : Double = lookup(angle, _sin)
  def cos(angle: Double) : Double = lookup(angle, _cos)
}
