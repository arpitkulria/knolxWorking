package controllers

import models._
import play.api._
import play.api.Play.current
import play.api.data._
import play.api.data.Forms._
import play.api.mvc._
import play.api.db.slick._
import play.api.db.slick.Config.driver.simple.Session
import play.mvc.Results.Redirect
import java.util.Date
import play.api.mvc.Flash

object Application extends Controller {

  val formForSignUp: Form[Knolx] = Form(
    mapping(

      "name" -> nonEmptyText,
      "address" -> nonEmptyText,
      "password" -> nonEmptyText,
      "company" -> nonEmptyText,
      "emailid" -> nonEmptyText,
      "phone" -> nonEmptyText,
      "created" -> ignored(new Date()),
      "updated" -> ignored(new Date()),
      "usertype" -> ignored(2),
      "knolx_id" -> ignored(0))(Knolx.apply)(Knolx.unapply))

  val formForSignIn: Form[KnolxSignIn] = Form(
    mapping(
      "emailid" -> nonEmptyText,
      "password" -> nonEmptyText)(KnolxSignIn.apply)(KnolxSignIn.unapply))

  def index: Action[AnyContent] = Action { implicit rs =>

    rs.session.get("usr").map { usr =>
      Ok(views.html.home("Hello " + usr))
    }.getOrElse {
      Ok(views.html.index())
    }

    //Ok(views.html.index())
  }

  def loginForm: Action[AnyContent] = Action { implicit rs =>

    Ok(views.html.loginFormPage(formForSignUp))

  }
  def home: Action[AnyContent] = Action { implicit rs =>
    rs.session.get("usr").map { usr =>
      Ok(views.html.home("Hello" + usr))
    }.getOrElse {
      Ok(views.html.index())
    }
  }

  def checkUser: Action[AnyContent] = DBAction { implicit rs =>

    formForSignIn.bindFromRequest().fold(
      hasErrors = { form => Ok("Error") },
      success = { newUsr =>
        val ans = Model.checkUserFunction(newUsr)
        println(ans)
        if (ans == 1) {
          Redirect(routes.Application.home).withSession("usr" -> newUsr.emailid)
        } else { Ok("FAILED") }
      })

  }

  def addUser: Action[AnyContent] = DBAction { implicit rs =>

    formForSignUp.bindFromRequest().fold(
      hasErrors = { form => Ok("Error") },
      success = { newUser =>
        val ans = Model.addUserFunction(newUser)

        Ok("success")
        //          val check=rs.session.get("usr").get
        //          if(check=="a@a.com"){
        //Redirect(routes.Application.loginForm)
        //else 
        //Redirect(routes.Application.index)
      })

  }

  def updateUser: Action[AnyContent] = DBAction { implicit rs =>
    val email = rs.session.get("usr").get
    val data = Model.fillForm(email)

    Ok(views.html.updateForm(formForSignUp.fill(data.head)))

  }

  def updateUserInfo: Action[AnyContent] = DBAction { implicit rs =>

    formForSignUp.bindFromRequest().fold(
      hasErrors = { form => Ok("Error") },
      success = { newUser =>
        println(newUser)
        println(newUser.created)
        val data = Model.fillForm(newUser.emailid).head
        println(data)
        println(data.knolx_id)
        println(data.created)
        
        val NewToUpdate: Knolx = newUser.copy(created=data.created,knolx_id = data.knolx_id)
        println(NewToUpdate)
        val ans = Model.updateUserFunction(NewToUpdate)

        if (ans == 1) Ok("Updated")
        else Ok("Not Updated")
      })

  }

  def logOut: Action[AnyContent] = Action {

    Redirect(routes.Application.index).withNewSession

  }

}