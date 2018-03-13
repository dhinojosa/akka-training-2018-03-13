package com.sbux.entities

class Employee private(val firstName: String, val lastName: String)

object Employee {
  def apply(firstName: String, lastName: String) =
    new Employee(firstName, lastName)

  def unapply(arg: Employee): Option[(String, String)] = {
    Some((arg.firstName, arg.lastName))
  }
}