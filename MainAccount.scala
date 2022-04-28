import MainAccount.actor
import ActorAccount._
import akka.actor.{Actor, ActorLogging, ActorRef, ActorSystem, Props}
object MainAccount extends App{
  val system = ActorSystem("actor-system")
  val actor = system.actorOf(Props[ActorAccount], name="actor")
  protected def createSenderActor(): ActorRef = system.actorOf(SenderActor.props(actor))
  private val senderActor = createSenderActor()

  senderActor ! "Start"


  system.terminate()

}

object SenderActor{
  def props(actor: ActorRef): Props = Props(new SenderActor(actor : ActorRef))
}

class SenderActor(actor: ActorRef) extends Actor with ActorLogging {

  import SenderActor._

  override def receive: Receive = {
    case "Start" => {
      actor ! createAccount("K", 21, "9234586789", "k@gmail.com")
      actor ! Deposit(500)
      actor !(ActorAccount.Withdraw(200))
      actor !(ActorAccount.Deposit(1000))
      actor !(ActorAccount.Withdraw(1200))
      actor !(ActorAccount.Deposit(1500))
      actor !(ActorAccount.Withdraw(2000))
      actor !(ActorAccount.Deposit(2000))
      actor !(ActorAccount.getDetails(1))
    }
    case Balance(amount) => log.info(s"$amount")
  }
}