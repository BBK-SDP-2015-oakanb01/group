// TODO
//
// Make this an actor and write a message handler for at least the
// set method.
//

import akka.actor.Actor
import akka.actor.ActorLogging

case class init(im: Image, of: String);
case class Set(x: Int, y: Int, color: Colour);
case class Print();

object Coordinator
{
  var image: Image = null
  var waiting: Int = 0
  var outfile: String = null
  
  def setImage(im: Image) = { image = im; }
  def getImage():Image = { image;}
  
  def setWait(wait: Int) = { waiting = wait; }
  def getWait():Int = { waiting;}
  
  def setOutfile(file: String) = { outfile = file; }
  def getOutfile():String = { outfile;}
}
class Coordinator extends Actor {
  /**
   * What to do if we receive a message!
   */
  override def receive = {
      case Set(x, y, c) => {
        val image = Coordinator.getImage();
        image(x, y) = c
        var waiting = Coordinator.getWait();
        waiting = waiting - 1
        Coordinator.setWait(waiting)
        println(Coordinator.getWait())
      }
      case init(im: Image, of: String) => {
        Coordinator.setImage(im)
        Coordinator.setOutfile(of)
        Coordinator.setWait(im.width * im.height)
      }
      case Print => {
        print
      }
      case _ => {
        println("???");
      }
  }

  /**
   * Matthew
   * I think we need to make an actor.
   * See first line in class...?
   *
  TODO: make set a message 
  def set(x: Int, y: Int, c: Colour) = {
    image(x, y) = c
    waiting -= 1
  }*/

  def print = {
    assert(Coordinator.getWait() == 0)
    val image = Coordinator.getImage();
    image.print(Coordinator.getOutfile())
  }
}
