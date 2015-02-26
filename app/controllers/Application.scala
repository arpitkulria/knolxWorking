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
      "created"->ignored(new Date()),
      "updated" -> ignored(new Date()),
      "usertype" ->ignored(2),
      "knolx_id" -> ignored(0))(Knolx.apply)(Knolx.unapply))
      
      
      val formForSignIn: Form[KnolxSignIn] = Form(
    mapping(
     "emailid" -> nonEmptyText,
      "password" -> nonEmptyText)(KnolxSignIn.apply)(KnolxSignIn.unapply))

  def index: Action[AnyContent] = Action {implicit rs=>
    
     rs.session.get("usr").map{usr=>
      Ok(views.html.home("Hello"+usr))
      }.getOrElse{Ok(views.html.index())
      }
    
    //Ok(views.html.index())
  }

   def loginForm: Action[AnyContent] = Action {implicit rs=>
     
    
     Ok(views.html.loginFormPage(formForSignUp))
          
         
    
  }
    def home: Action[AnyContent] = Action {implicit rs=>
      rs.session.get("usr").map{usr=>
      Ok(views.html.home("Hello"+usr))
      }.getOrElse{ Ok(views.html.index())
      }
  }
    
    
    def checkUser : Action[AnyContent] = DBAction { implicit rs =>
    
    formForSignIn.bindFromRequest().fold(
        hasErrors={form=>Ok("Error")},
        success={newUsr=> val ans=Model.checkUserFunction(newUsr)
        if(ans==1){Redirect(routes.Application.home).withSession("usr"-> newUsr.emailid)}
        else{Ok("FAILED")}
     })
          
 } 
  
  def addUser : Action[AnyContent] = DBAction { implicit rs =>
    
    formForSignUp.bindFromRequest().fold(
        hasErrors={form=>Ok("Error")},
        success={newUser=> val ans=Model.addUserFunction(newUser)
          
          Ok("success")
//          val check=rs.session.get("usr").get
//          if(check=="a@a.com"){
     //Redirect(routes.Application.loginForm)
          //else 
            //Redirect(routes.Application.index)
     })
          
 }
  
  def updateUser : Action[AnyContent] = DBAction { implicit rs =>
    val email=rs.session.get("usr").get
   val data= Model.fillForm(email)
   
    Ok(views.html.updateForm(formForSignUp.fill(data.head)))
          
 }
  
  
  def updateUserInfo : Action[AnyContent] = DBAction { implicit rs =>
    
   
    formForSignUp.bindFromRequest().fold(
        hasErrors={form=>Ok("Error")},
        success={newUser=> 
          println(newUser)
        val data=Model.fillForm(newUser.emailid).head
          println(data)
          val NewToUpdate: Knolx = newUser.copy(knolx_id=data.knolx_id)
        println(NewToUpdate)
          val ans=Model.updateUserFunction(NewToUpdate)
          
          if (ans==1) Ok("Updated")
          else Ok("Not Updated")
  })
          
 }
  
  def logOut:Action[AnyContent]=Action{
    
    
    Redirect(routes.Application.index).withNewSession
    
  }
  
  
//  def updateEmp(id:Int): Action[AnyContent] = DBAction { implicit rs =>
//       formForEdit.bindFromRequest().fold(hasErrors={form=>Ok("Error")},
//        success={newUser=> 
//          val NewToUpdate: Knol = newUser.copy(knol_id=id) 
//          Model.updateEmpMethod(id,NewToUpdate)
//          Redirect(routes.Application.showEmp(0,5,"",30)).flashing("Success"-> "Employee Updated")})
//    }
  
  
 
//  def upEmp(id:Int): Action[AnyContent] = Action {
//
//    Ok(views.html.editEmp(id,formForEdit))
//
//  }
//
//  def showEmp(page:Int,pageSize:Int,filt:String,total:Int): Action[AnyContent] = DBAction { implicit rs =>
//    val ans = Model.showEmp(page,pageSize,"%"+filt+"%",total)
//    Ok(views.html.showEmp(ans,page,pageSize,filt,total))
//  }
//  
//  def deleteEmp(id:Int): Action[AnyContent] = DBAction { implicit rs =>
//          Model.deleteEmpfromDB(id)
//          Redirect(routes.Application.showEmp(0,5,"",30)).flashing("Success"-> "Employee deleted")
// }
//  
//   def findEmp(name:String): Action[AnyContent] = DBAction { implicit rs =>
//          val ans=Model.findEmpfromDB(name)
//          Ok(views.html.showEmp(ans,0,5,"",30))
// }
//     def updateEmp(id:Int): Action[AnyContent] = DBAction { implicit rs =>
//       formForEdit.bindFromRequest().fold(hasErrors={form=>Ok("Error")},
//        success={newUser=> 
//          val NewToUpdate: Knol = newUser.copy(knol_id=id) 
//          Model.updateEmpMethod(id,NewToUpdate)
//          Redirect(routes.Application.showEmp(0,5,"",30)).flashing("Success"-> "Employee Updated")})
//    }
//      def loginForm: Action[AnyContent] = Action {
//
//    Ok(views.html.loginFormPage())
//
//  }
//    
  
  
     
     

}