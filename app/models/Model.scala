package models

import java.util.Date
import java.sql.{ Date => SqlDate }
import scala.slick.lifted.Tag
import play.api.Play.current
import play.api.db.slick.Config.driver.simple._
import java.sql.Timestamp

/**
 * Model
 * @author knoldus
 */

object Model extends App {

  implicit lazy val util2sqlDateMapper = MappedColumnType.base[java.util.Date, java.sql.Date](
    { utilDate => new java.sql.Date(utilDate.getTime()) },
    { sqlDate => new java.util.Date(sqlDate.getTime()) })

  /**
   * Class for mapping to database table
   */

  class KnolxTable(tag: Tag) extends Table[Knolx](tag, "knolx") {
    def knolx_id: Column[Int] = column[Int]("knolx_id", O.PrimaryKey, O.AutoInc)
    def name: Column[String] = column[String]("name", O.NotNull)
    def address: Column[String] = column[String]("address", O.NotNull)
    def password: Column[String] = column[String]("password", O.NotNull)
    def company: Column[String] = column[String]("company", O.NotNull)
    def emailid: Column[String] = column[String]("emailid", O.NotNull)
    def phone: Column[String] = column[String]("phone", O.NotNull)
    def created: Column[Date] = column[Date]("created", O.NotNull)
    def updated: Column[Date] = column[Date]("updated", O.NotNull)
    def userType: Column[Int] = column[Int]("userType", O.NotNull)
    def uniqueName = index("emailid", emailid, unique = true)

    def * = (name, address, password, company, emailid, phone, created, updated, userType, knolx_id) <> (Knolx.tupled, Knolx.unapply)
  }

  /**
   * function to update data
   *
   * @param id
   * @param knolxObj
   * @param s
   * @return
   */
  def updateUserData(id: Int, knolxObj: Knolx)(implicit s: Session): Int = {
    val knolxTableQueryObj = TableQuery[KnolxTable]
    knolxTableQueryObj.filter(_.knolx_id === id).update(knolxObj)
  }
  /**
   * function to validate user
   *
   * @param obj
   * @param s
   * @return
   */
  def checkUserFunction(obj: KnolxSignIn)(implicit s: Session): Int = {
    val knolxTableQueryObj = TableQuery[KnolxTable]

    knolxTableQueryObj.filter(x => x.emailid === obj.emailid && x.password === obj.password).list.length

  }

  /**
   * Function to add new user
   *
   * @param knolObj
   * @param s
   * @return
   */
  def addUserFunction(knolObj: Knolx)(implicit s: Session): Int = {
    val knolxTableQueryObj = TableQuery[KnolxTable]
    knolxTableQueryObj.insert(knolObj)

  }

  /**
   * Function to get knolx object by email ID
   *
   * @param email
   * @param s
   * @return
   */
  def fillForm(email: String)(implicit s: Session): List[Knolx] = {
    val knolxTableQueryObj = TableQuery[KnolxTable]

    knolxTableQueryObj.filter(x => x.emailid === email).list

  }

  /**
   * Function to update user information
   *
   * @param obj
   * @param s
   * @return
   */
  def updateUserFunction(obj: Knolx)(implicit s: Session): Int = {

    val knolxTableQueryObj = TableQuery[KnolxTable]
    knolxTableQueryObj.filter { _.knolx_id === obj.knolx_id }.update(obj)

  }

}

/**
 * case class for knolx (for add user and update user data)
 */

case class Knolx(name: String, address: String, password: String, company: String, emailid: String, phone: String, created: Date = new Date(), updated: Date = new Date(), userType: Int = 2, knolx_id: Int = 0)

/**
 * case class for add user and update user data)
 */
case class KnolxSignIn(emailid: String, password: String)