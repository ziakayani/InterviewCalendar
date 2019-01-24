package model

/**
 * IO Manager Trait,
 * Later on can be used to write to file or Database
 *
 */
trait IOMananger {
  /**
   * Add an Interviewer
   *
   *  @param PersonDTO
   */
  def addInterviewer(person: PersonDTO): Unit
  /**
   * Add a Candidate
   *
   *  @param PersonDTO
   */
  def addCandidate(person: PersonDTO): Unit
  /**
   * Get the already added Interviewer
   *
   *  @param email
   *  @return PersonDTO
   */
  def getInterviewer(email: String): PersonDTO
  /**
   * Get the already added Candidate
   *
   *  @param email
   *  @return PersonDTO
   */
  def getCandidate(email: String): PersonDTO
  /**
   * Get all the possible interview time overlaps for a person
   *
   *  @param email
   *  @param PersonType
   *  @return ResponseDTO
   */
  def getInterviewAvailability(email: String, personType: PersonType.PersonType): ResponseDTO
}