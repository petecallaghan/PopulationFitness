package uk.edu.populationfitness.test

import org.scalatest.FunSpec
import uk.edu.populationfitness.models.maths.FastMaths

class FastMathsSpec extends FunSpec{
  describe("given fast maths power functions"){

    describe("given an integer power"){
      it("should raise by 0"){
        assert(1 == FastMaths.pow(5, 0))
        assert(1.0 == FastMaths.pow(5.0, 0))
      }
      it("should raise by 1"){
        assert(5 == FastMaths.pow(5, 1))
        assert(5.0 == FastMaths.pow(5.0, 1))
      }
      it("should raise by 2"){
        assert(25 == FastMaths.pow(5, 2))
        assert(25.0 == FastMaths.pow(5.0, 2))
      }
      it("should raise by 3"){
        assert(125 == FastMaths.pow(5, 3))
        assert(125.0 == FastMaths.pow(5.0, 3))
      }
      it("should raise by 4"){
        assert(625 == FastMaths.pow(5, 4))
        assert(625.0 == FastMaths.pow(5.0, 4))
      }
      it("should raise by 5"){
        assert(3125 == FastMaths.pow(5, 5))
        assert(3125.0 == FastMaths.pow(5.0, 5))
      }
      it("should raise by max"){
        assert(math.pow(5.0, java.lang.Long.MAX_VALUE) == FastMaths.pow(5.0, java.lang.Long.MAX_VALUE))
      }
      it("should raise by big"){
        assert(math.pow(2.0, 32500000L) == FastMaths.pow(2.0, 32500000L))
      }
    }
  }
}
