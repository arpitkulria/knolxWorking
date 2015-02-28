package models

import java.util.Date
import java.sql.{ Date => SqlDate }
import scala.slick.lifted.Tag
import play.api.Play.current
import play.api.db.slick.Config.driver.simple._
import java.sql.Timestamp

/**
 * @author knoldus
 */
object Model extends App {

  implicit lazy val util2sqlDateMapper = MappedColumnType.base[java.util.Date, java.sql.Date](
    { utilDate => new java.sql.Date(utilDate.getTime()) },
    { sqlDate => new java.util.Date(sqlDate.getTime()) })

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
   * @param knolObj
   * @param s
   */

  /**
   * @param s
   * @return
   */
  /*def showEmp(page:Int,pageSize:Int,filter:String,total:Int)(implicit s: Session):List[Knol]={
    val knolTableQueryObj = TableQuery[KnolTable]
    val offset=pageSize * page
     knolTableQueryObj.filter(_.name.toUpperCase like filter.toUpperCase).drop(offset).take(pageSize).list
    
  }*/

  /*def deleteEmpfromDB(id:Int)(implicit s: Session):Int={
    val knolTableQueryObj = TableQuery[KnolTable]
     knolTableQueryObj.filter(_.knol_id===id).delete 
  }*/
  /*def findEmpfromDB(name:String)(implicit s: Session):List[Knol]={
    val knolTableQueryObj = TableQuery[KnolTable]
   
     knolTableQueryObj.filter(_.name.toUpperCase like "%"+name.toUpperCase()+"%").list
  }*/

  def updateUserData(id: Int, knolxObj: Knolx)(implicit s: Session): Int = {
    val knolxTableQueryObj = TableQuery[KnolxTable]
    knolxTableQueryObj.filter(_.knolx_id === id).update(knolxObj)
  }

  def checkUserFunction(obj: KnolxSignIn)(implicit s: Session): Int = {
    val knolxTableQueryObj = TableQuery[KnolxTable]

    knolxTableQueryObj.filter(x => x.emailid === obj.emailid && x.password === obj.password).list.length

  }
  def addUserFunction(knolObj: Knolx)(implicit s: Session): Int = {
    val knolxTableQueryObj = TableQuery[KnolxTable]
    knolxTableQueryObj.insert(knolObj)

  }

  def fillForm(email: String)(implicit s: Session): List[Knolx] = {
    val knolxTableQueryObj = TableQuery[KnolxTable]

    knolxTableQueryObj.filter(x => x.emailid === email).list

  }

  def updateUserFunction(obj: Knolx)(implicit s: Session): Int = {

    val knolxTableQueryObj = TableQuery[KnolxTable]
    //knolxTableQueryObj.filter(_.knolx_id===obj.knolx_id).delete 
    //knolxTableQueryObj.insert(obj)
    knolxTableQueryObj.filter { _.knolx_id === obj.knolx_id }.update(obj)

  }

}
case class Knolx(name: String, address: String, password: String, company: String, emailid: String, phone: String, created: Date = new Date(), updated: Date = new Date(), userType: Int = 2, knolx_id: Int = 0)

case class KnolxSignIn(emailid: String, password: String)