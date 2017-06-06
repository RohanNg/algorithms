package sorting.quickSort

import java.io.FileReader
import java.util.Scanner

object QuickSort extends App {

  /**
    * Sort array, returning number of comparision executed
    * during sort procedure.
    *
    * @param f function determines the pivot element for the sort procedure
    * @param arr aray to be sorted
    * @return number of comparision executed
    */
  def sort(f: (Array[Int], Int, Int) => Int)(arr: Array[Int]): Int = {
    val swap = (x: Int, y: Int) =>
      if (x != y) {
        val e = arr(x)
        arr(x) = arr(y)
        arr(y) = e
      }

    def rec(from: Int, until: Int): Int =
      if (until - from <= 1) 0
      else {
        swap(f(arr, from, until), from)
        val pivot = arr(from)
        var i = from + 1
        var j = i;
        while (j < until) {
          if (arr(j) < pivot) {
            swap(j, i)
            i = i + 1
          }
          j = j + 1
        }
        swap(from, i - 1)
        rec(from, i - 1) + rec(i, until) + (until - from - 1)
      }

    rec(0, arr.length)
  }

  override def main(args: Array[String]): Unit = {
    def getTestData(): Array[Int] = {
      val in = new Scanner(new FileReader("./testData/sort/QuickSort10k.txt"))
      val ints = new Array[Int](10000)
      var count = 0
      while (in.hasNext) {
        ints(count) = in.next().toInt;
        count = count + 1
      }
      in.close()
      ints
    }

    def smartPivot(arr: Array[Int], from: Int, until: Int): Int =
      (until - from) match {
        case n if n < 2 => throw new RuntimeException
        case n if n == 2 => from
        case n => {
          assert(n >= 3)
          val mid = (until + from - 1) / 2
          val a = arr(from)
          val b = arr(mid)
          val c = arr(until - 1)
          if (a >= c)
            if (c >= b) until - 1 // a >= c >= b
            else if (a >= b) mid // a >= b > c
            else from // b > a >= c
          else {
            if (a >= b) from // c > a >= b
            else if (b >= c) until - 1 // b >= c > a
            else mid // c > b > a
          }
        }
      }

    println(sort((_, from, _) => from)(getTestData()))
    println(sort((_, _, until) => until - 1)(getTestData()))
    val in = getTestData()
    println(sort(smartPivot)(in))
    var i = 1
    for (x <- in) {
      assert(x == i)
      i = i + 1
    }
  }
}
