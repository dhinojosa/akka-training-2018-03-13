package com.sbux.config

import com.typesafe.config.ConfigFactory
import org.scalatest.{FunSuite, Matchers}

class ConfigSpec extends FunSuite with Matchers {


  test("parse of configuration string") {
    val str = """a{b = 4}"""
    val config = ConfigFactory.parseString(str)
    config.getInt("a.b") should be (4)
  }
}
