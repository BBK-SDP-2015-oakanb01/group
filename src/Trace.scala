
import akka.actor.ActorSystem
import akka.actor.ActorRef
import akka.actor.Props
import com.typesafe.config.ConfigFactory

//  stdout-loglevel = "OFF";
//  loglevel = "OFF";


object Trace {

  val AntiAliasingFactor = 4
  val Width = 200
  val Height = 200
    
  var rayCount = 0
  var hitCount = 0
  var lightCount = 0
  var darkCount = 0

  def main(args: Array[String]): Unit = {
    if (args.length != 2) {
      println("usage: scala Trace input.dat output.png")
      System.exit(-1)
    }

    val (infile, outfile) = (args(0), args(1))
    val scene = Scene.fromFile(infile)

    render(scene, outfile, Width, Height)

    println("rays cast " + rayCount)
    println("rays hit " + hitCount)
    println("light " + lightCount)
    println("dark " + darkCount)
  }

  def render(scene: Scene, outfile: String, width: Int, height: Int) = {
    val image = new Image(width, height)
    
    // Init the coordinator -- must be done before starting it.
    val mySystem  = ActorSystem("CoordinationSytem");
    val coordinator = mySystem.actorOf(Props[Coordinator], name = "coordinator");
    coordinator ! init(image, outfile)

    // TODO: Start the Coordinator actor.
    

    scene.traceImage(width, height)
    coordinator ! Print
    // TODO:
    // This one is tricky--we can't simply send a message here to print
    // the image, since the actors started by traceImage haven't necessarily
    // finished yet.  Maybe print should be called elsewhere?
    // coordinator.print
    mySystem.shutdown();
  }
}