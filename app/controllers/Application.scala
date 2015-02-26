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
import play.mvc.Results.Redirect

object Application extends Controller {

  /**
 * Mapping to signUp form
 */
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

  /**
   * validate user and redirect to index
 * @return
 */
def index: Action[AnyContent] = Action { implicit rs =>

    rs.session.get("usr").map { usr =>
      Ok(views.html.home("Hello " + usr))
    }.getOrElse {
      Ok(views.html.index())
    }

} 

  /**
   * render login form
 * @return
 */
def loginForm: Action[AnyContent] = Action { implicit rs =>

    Ok(views.html.loginFormPage(formForSignUp))
}
  /**
   * validate user if true then redirect to home else redirect to index page
 * @return
 */
def home: Action[AnyContent] = Action { implicit rs =>
    rs.session.get("usr").map { usr =>
      Ok(views.html.home(usr))
    }.getOrElse {
      Ok(views.html.index())
    }
}

  /**
   * check user exist or not
 * @return
 */
def checkUser: Action[AnyContent] = DBAction { implicit rs =>

    formForSignIn.bindFromRequest().fold(
      hasErrors = { form => Redirect(routes.Application.loginForm).flashing("error"-> "Invalid User Name and Password") },
      success = { newUsr =>
        val ans = Model.checkUserFunction(newUsr)
        println(ans)
        if (ans == 1) {
          Redirect(routes.Application.home).withSession("usr" -> newUsr.emailid)
        } else { Redirect(routes.Application.loginForm).flashing("error"-> "Invalid User Name and Password") }
      })

}

  /**
 * add new user (Register)
 * @return
 * 
 * 
 *  <a class="navbar-brand" href="@routes.Application.index">Knolx</a>
 */
def addUser: Action[AnyContent] = DBAction { implicit rs =>

    formForSignUp.bindFromRequest().fold(
      hasErrors = { form => Redirect(routes.Application.loginForm).flashing("error"-> "Error in Information") },
      success = { newUser =>
        val ans = Model.addUserFunction(newUser)
        Redirect(routes.Application.loginForm).flashing("msg"-> "Successfully Registered please login with your user and password")}
)

}

  /**
   * render update form to page
 * @return
 */
def updateUser: Action[AnyContent] = DBAction { implicit rs =>
    val email = rs.session.get("usr").get
    val data = Model.fillForm(email)

    Ok(views.html.updateForm(formForSignUp.fill(data.head)))

  }

  /**
   * 
   * update user information
 * @return
 */
def updateUserInfo: Action[AnyContent] = DBAction { implicit rs =>

    formForSignUp.bindFromRequest().fold(
      hasErrors = { form => Redirect(routes.Application.loginForm).flashing("error"-> "Error in Information Update") },
      success = { newUser =>
        val data = Model.fillForm(newUser.emailid).head
        val NewToUpdate: Knolx = newUser.copy(created = data.created, knolx_id = data.knolx_id)
        val ans = Model.updateUserFunction(NewToUpdate)
        if (ans == 1) Redirect(routes.Application.loginForm).flashing("msg"-> "Information Updated")
        else Redirect(routes.Application.loginForm).flashing("error"-> "Error in Information Update")
      })

  }

  /**
   * when clicked to logout redirect to index and invalidate session
 * @return
 */
def logOut: Action[AnyContent] = Action {

    Redirect(routes.Application.index).withNewSession

  }

def listOfSession: Action[AnyContent] = Action { implicit rs =>
     rs.session.get("usr").map { usr =>
      Ok(views.html.listOfSession())
    }.getOrElse {
      Ok(views.html.index())
    }



  }


}