import ActorAccount.{Balance, Deposit, Withdraw, accountBalance, createAccount, getDetails}
import akka.actor.{ActorSystem, Props}
import akka.testkit.{ImplicitSender, TestActors, TestKit, TestProbe}
import org.scalatest.BeforeAndAfterAll
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpecLike

class ActorAccountSpec()
  extends TestKit(ActorSystem("BankSpec"))
    with ImplicitSender
    with AnyWordSpecLike
    with Matchers
    with BeforeAndAfterAll {

  override def afterAll(): Unit = {
    TestKit.shutdownActorSystem(system)
  }
  "Initial balance" must {
    "initialise with 0" in {
      val sender = TestProbe()
      val bank = system.actorOf(Props[ActorAccount])
      sender.send(bank, createAccount("Khushi",21,"9742836364","k@gmail.com"))
      sender.send(bank, getDetails(1))

      sender.expectMsg(Balance(0))
    }
    "Valid amount deposited" in {
      val sender = TestProbe()
      val bank = system.actorOf(Props[ActorAccount])
      sender.send(bank, createAccount("Khushi",21,"9742836364","k@gmail.com"))
      sender.send(bank, Deposit(1000))
      sender.send(bank, getDetails(1))

      sender.expectMsg(Balance(1000))
    }
    "Valid amount withdrawn" in {
      val sender = TestProbe()
      val bank = system.actorOf(Props[ActorAccount])
      sender.send(bank, createAccount("Khushi",21,"9742836364","k@gmail.com"))
      sender.send(bank, Deposit(1000))
      sender.send(bank,Withdraw(400))
      sender.send(bank, getDetails(1))

      sender.expectMsg(Balance(600))
    }
    "Deposit exceeds account limit" in {
      val sender = TestProbe()
      val bank = system.actorOf(Props[ActorAccount])
      sender.send(bank, createAccount("Khushi",21,"9742836364","k@gmail.com"))
      sender.send(bank, Deposit(1000))
      sender.send(bank, Deposit(5000))
      sender.send(bank, getDetails(1))

      sender.expectMsg(Balance(1000))
    }
    "Insufficient funds" in {
      val sender = TestProbe()
      val bank = system.actorOf(Props[ActorAccount])
      sender.send(bank, createAccount("Khushi",21,"9742836364","k@gmail.com"))
      sender.send(bank, Deposit(1500))
      sender.send(bank,Withdraw(2000))
      sender.send(bank, getDetails(1))

      sender.expectMsg(Balance(1500))
    }
  }
}






