package multiplication.integer

import java.util
import java.util.LinkedList

import scala.annotation.tailrec

object IntegerMultiplication extends App{

  object MyInteger {
    def apply(digits: LinkedList[Int]) = new MyInteger(digits)
    def apply(digits: String) = {
      val value = new LinkedList[Int]()
      for( digit <- digits.map(b => b.toInt - '0'.toInt))
        value.add(digit)
      new MyInteger(value)
    }
    def apply(digits: Int): MyInteger = apply(digits)
  }

  /**
    * Immutable integer
    * Fast addition and multiplication
    * Multiplication was implemented using Karatsuba algorithm
    *
    * A * B can be calculated by
    * A = a*10^x + b
    * B = c*10^x + d
    * A*B = ac*10^(2x) + [(a + b)(c + d) - ac - db]*10^x + db
    */
  class MyInteger(private val digits: LinkedList[Int]) {
    process(digits)

    private def process(rawDigit: LinkedList[Int]) : Unit = {
      if(rawDigit.isEmpty) rawDigit.add(0)
      else if (rawDigit.getFirst == 0) {
        while(rawDigit.size() > 1 && rawDigit.getFirst == 0) {
          rawDigit.removeFirst()
        }
      }
    }

    /* Multiply by 10 given number of time */
    def x10(pow: Int): MyInteger = {
      assert(pow >= 0)
      val copied = new LinkedList[Int](digits)

      @tailrec
      def x10Rec(linkedList: LinkedList[Int], pow: Int): LinkedList[Int] =
        if (pow > 0) {
          linkedList.addLast(0)
          x10Rec(linkedList, pow - 1)
        } else linkedList
      new MyInteger(x10Rec(copied, pow))
    }

    def this(digits : String) = {
      this(
        digits.foldLeft(new LinkedList[Int]())
          ((linkedList, c) => { linkedList.add(c.toInt - '0'.toInt); linkedList})
      )
    }

    def + (that: MyInteger): MyInteger = {
      val value = new LinkedList[Int]()
      val thatIt = that.digits.listIterator(that.digits.size)
      val thisIt = this.digits.listIterator(this.digits.size)
      def rec(a: util.ListIterator[Int], b: util.ListIterator[Int], linkedList: LinkedList[Int], carry: Int): LinkedList[Int] = {
        def add(res: Int): LinkedList[Int] = {
          if(res > 9) {
            linkedList.addFirst(res % 10)
            rec(a, b, linkedList, 1)
          } else {
            linkedList.addFirst(res)
            rec(a, b, linkedList, 0)
          }
        }
        if(a.hasPrevious) {
          if(b.hasPrevious) add(a.previous() + b.previous() + carry)
          else add(a.previous() + carry)
        } else if (b.hasPrevious) {
          add(b.previous() + carry)
        } else if(carry != 0) {
          assert(carry == 1)
          linkedList.addFirst(carry)
          linkedList
        } else linkedList
      }
      new MyInteger(rec(thisIt, thatIt, value, 0))
    }

    def - (that: MyInteger): MyInteger = {
      val value = new LinkedList[Int]()
      val minuend = this.digits.listIterator(this.digits.size)
      val subtrahend = that.digits.listIterator(that.digits.size)
      def rec(minuend: util.ListIterator[Int], subtrahend: util.ListIterator[Int], value: LinkedList[Int], carry: Int): LinkedList[Int] = {
        def sub(res: Int): LinkedList[Int] = {
          if(res >= 0) {
            assert(res < 10)
            value.addFirst(res)
            rec(minuend, subtrahend, value, 0)
          } else {
            value.addFirst(res + 10)
            rec(minuend, subtrahend, value, 1)
          }
        }
        if(minuend.hasPrevious) {
          if(subtrahend.hasPrevious) sub(minuend.previous() - subtrahend.previous() - carry)
          else sub(minuend.previous() - carry)
        } else if (subtrahend.hasPrevious) {
          throw new IllegalArgumentException("this - that < 0")
        } else if(carry != 0) {
          throw new IllegalArgumentException("this - that < 0")
        } else value
      }
      new MyInteger(rec(minuend, subtrahend, value, 0))
    }

    private def mulWithDigit (digit: Int): MyInteger = {
      assert(digit >= 0 && digit <= 9)
      val value = new LinkedList[Int]()
      val thisIt = this.digits.listIterator(this.digits.size)
      def rec(linkedList: LinkedList[Int], carry: Int): LinkedList[Int] = {
        def process(res: Int): LinkedList[Int] = {
          if(res > 9) {
            linkedList.addFirst(res % 10)
            rec(linkedList, res / 10)
          } else {
            linkedList.addFirst(res)
            rec(linkedList, 0)
          }
        }
        if(thisIt.hasPrevious) {
          process(thisIt.previous * digit + carry)
        } else if (carry > 0) {
          process(carry)
        } else linkedList
      }
      new MyInteger(rec(value, 0))
    }

    private def split(index: Int): (MyInteger, MyInteger) = {
      val leftIt = digits.listIterator(index)
      val leftNum = new util.LinkedList[Int]()
      while(leftIt.hasPrevious) leftNum.addFirst(leftIt.previous())
      val rightIt = digits.listIterator(index)
      val rightNum = new util.LinkedList[Int]()
      while(rightIt.hasNext) rightNum.addLast(rightIt.next())
      (new MyInteger(leftNum), new MyInteger(rightNum))
    }

    def * (that: MyInteger): MyInteger = {
      val mid = math.min(this.digits.size()/2, that.digits.size/2)
      if(mid == 0) {
        // exists number contain single digit
        if(that.digits.size() == 1) this.mulWithDigit(that.digits.get(0))
        else that.mulWithDigit(this.digits.get(0))
      } else {
        val (a, b) = this.split(this.digits.size() - mid)
        val (c, d) = that.split(that.digits.size() - mid)
        val mul1 = a * c
        val mul2 = (a + b) * (c + d)
        val mul3 = b * d
        val res = mul1.x10(2*mid) + (mul2 - mul1 - mul3).x10(mid) + mul3
        println(s"$this * $that = $res")
        res
      }
    }

    override def toString: String = digits.toString.replaceAll(", ","")
  }


  override def main(args: Array[String]): Unit = {
    println((new MyInteger("3141592653589793238462643383279502884197169399375105820974944592") * new MyInteger("2718281828459045235360287471352662497757247093699959574966967627")).toString)
  }
}
