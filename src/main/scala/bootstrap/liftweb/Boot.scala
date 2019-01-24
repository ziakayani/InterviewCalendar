package bootstrap.liftweb

import net.liftweb.common.Loggable
import net.liftweb.http.LiftRules
import net.liftweb.http.LiftRulesMocker.toLiftRules
import restful.Router

/**
 * A class that's instantiated early and run. It allows the application
 * to modify lift's environment
 */
class Boot extends Loggable {

  /**
   * Function to define all the routes and application level setting.
   *
   */
  def boot {

    // Force the request to be UTF-8
    LiftRules.early.append(_.setCharacterEncoding("UTF-8"))

    // Initialize custom Router
    Router.init()
  }
}