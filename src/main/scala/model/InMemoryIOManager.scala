package model

import org.joda.time.Interval
import scala.collection.mutable.ListBuffer

/**
 * InMemory Manager
 *
 * Implements the IOManager,
 *
 */

class InMemoryIOManager extends IOMananger {

  // store the interviewers and candidates
  var interviewers = Map.empty[String, PersonDTO]
  var candidates = Map.empty[String, PersonDTO]

  /**
   * Add an Interviewer
   *
   *  @param PersonDTO
   */
  def addInterviewer(person: PersonDTO): Unit = {
    interviewers = interviewers + (person.email -> person)
  }
  /**
   * Add a Candidate
   *
   *  @param PersonDTO
   */
  def addCandidate(person: PersonDTO): Unit = {
    candidates = candidates + (person.email -> person)
  }
  /**
   * Get the already added Interviewer
   *
   *  @param email
   *  @return PersonDTO
   */
  def getInterviewer(email: String): PersonDTO = {
    if (interviewers.contains(email))
      interviewers(email)
    else
      null
  }
  /**
   * Get the already added Candidate
   *
   *  @param email
   *  @return PersonDTO
   */
  def getCandidate(email: String): PersonDTO = {
    if (candidates.contains(email))
      candidates(email)
    else
      null
  }

  /**
   * Get all the possible interview time overlaps for a person
   *
   *  @param email
   *  @param PersonType
   *  @return ResponseDTO
   */
  def getInterviewAvailability(email: String, personType: PersonType.PersonType): ResponseDTO = {
    personType match {
      case PersonType.Interviewer => {
        val interviewer = getInterviewer(email)
        if (interviewer != null) {
          var persons = ListBuffer.empty[PersonDTO]
          interviewer.availability.foreach(interviewerDuration => {
            candidates.values.foreach(candidate => {
              var durations = ListBuffer.empty[Duration]
              candidate.availability.foreach(candidateDuration => {
                val overlapInterval = interviewerDuration.interal.overlap(candidateDuration.interal)
                // if duration is overlapped, add to the result
                if (overlapInterval != null)
                  durations += Duration(overlapInterval.getStart, overlapInterval.getEnd)
              })
              if (durations.size != 0) {
                persons += (PersonDTO(candidate.email, candidate.personType, durations))
              }
            })
          })
          // grouping all the intervals for a single person into single place
          val organizedPersons = persons.map(x => ((x.email, x.personType), x.availability))
            .groupBy(_._1).map(x => {
              PersonDTO(x._1._1, x._1._2, x._2.map(_._2).flatten)
            }).toList
          ResponseDTO(interviewer.email, interviewer.personType, organizedPersons)
        } else null
      }
      case PersonType.Candidate =>
        {
          val candidate = getCandidate(email)
          if (candidate != null) {
            var persons = ListBuffer.empty[PersonDTO]
            candidate.availability.foreach(candidateDuration => {
              interviewers.values.foreach(interviewer => {
                var durations = ListBuffer.empty[Duration]
                interviewer.availability.foreach(interviewerDuration => {
                  val overlapInterval = candidateDuration.interal.overlap(interviewerDuration.interal)
                  // if duration is overlapped, add to the result
                  if (overlapInterval != null)
                    durations += Duration(overlapInterval.getStart, overlapInterval.getEnd)
                })
                if (durations.size != 0) {
                  persons += (PersonDTO(interviewer.email, interviewer.personType, durations))
                }
              })
            })

            // grouping all the intervals for a single person into single place
            val organizedPersons = persons.map(x => ((x.email, x.personType), x.availability))
              .groupBy(_._1).map(x => {
                PersonDTO(x._1._1, x._1._2, x._2.map(_._2).flatten)
              }).toList
            ResponseDTO(candidate.email, candidate.personType, organizedPersons)
          } else null
        }
    }
  }
}