package restful

import controller.Controller
import model.PersonType
import net.liftweb.http.JsonResponse
import net.liftweb.http.LiftRules
import net.liftweb.http.LiftRulesMocker.toLiftRules
import net.liftweb.http.js.JsExp.strToJsExp
import net.liftweb.http.rest.RestHelper
import net.liftweb.json.JsonAST.render
import net.liftweb.json.Printer.compact

/**
 *
 * Router Object defines all the API end points
 *
 */
object Router extends RestHelper {

  def init(): Unit = {
    // Adding this router to LiftRules, so that It consider it as a RestAPI
    LiftRules.statelessDispatch.append(Router)
  }

  serve {
    "v1" / "api" prefix {

      // Add the new Person whether an Interviewer or Candidate
      case "add" :: Nil JsonPut json -> _ => {
        JsonResponse(Controller.addPerson(compact(render(json))))
      }

      // get the details of an Interviewer
      case "getInterviewer" :: Nil Get request => for {
        email <- request.param("email") ?~ "email not found"
      } yield JsonResponse(Controller.getPerson(email, PersonType.Interviewer));

      // get the details of a Candidate
      case "getCandidate" :: Nil Get request => for {
        email <- request.param("email") ?~ "email not found"
      } yield JsonResponse(Controller.getPerson(email, PersonType.Candidate));

      // get All the possible Time Intervals
      case "getPossibleInterViews" :: Nil Get request => for {
        email <- request.param("email") ?~ "email not found"
        personType <- request.param("personType") ?~ "personType not found"
      } yield JsonResponse(Controller.getInterviewAvailability(email, personType));
    }
  }
}
