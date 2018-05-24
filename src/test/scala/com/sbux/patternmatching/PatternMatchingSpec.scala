package com.sbux.patternmatching

import com.sbux.entities.Employee
import org.scalatest.{FunSuite, Matchers}

class PatternMatchingSpec extends FunSuite with Matchers {


  test("A basic pattern match") {
    val e = Employee("Marc", "Nagel")
    val result = e match {
      case Employee(fn, ln) => s"Captured Employee $fn $ln"
      case _ => "Don't know"
    }
    result should be ("Captured Employee Marc Nagel")
  }
}
