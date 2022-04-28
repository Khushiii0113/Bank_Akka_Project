import ActorAccount.{Balance, accountBalance, accountId, accountOwnerAge, accountOwnerEmail, accountOwnerName, accountOwnerPhone}
import akka.actor.Actor
import akka.event.LoggingReceive


object ActorAccount{
  var accountBalance = 0
  var maxAccountBalance = 2000
  var accountId = 1
  var accountOwnerName = ""
  var accountOwnerAge = 0
  var accountOwnerPhone = ""
  var accountOwnerEmail = ""
  case class createAccount(name: String, age: Int, phone: String, email: String)
  case class Withdraw(amt: Int){
    require(amt>0)
  }
  case class Deposit(amt:Int)
  case class getDetails(accountId: Int)
  case class deleteAccount(accountId: Int)
  case class Balance(accountBalance: Int)
}

class ActorAccount extends Actor{
  def receive = LoggingReceive{
    case ActorAccount.createAccount(name, age, phone, email) => {
      accountOwnerName = name
      accountOwnerAge = age
      accountOwnerPhone = phone
      accountOwnerEmail = email
      accountBalance = 0
//      println(s"Your account ID is ${accountId}")
      accountId += 1
    }

    case ActorAccount.Withdraw(amt) => {
      if (ActorAccount.accountBalance >= amt) {
        ActorAccount.accountBalance -= amt
        println(s"The balance is deducted by $amt and the remaining balance is " + ActorAccount.accountBalance)
      }
      else {
        println(s"The balance in your account is " + ActorAccount.accountBalance + s". You don't have sufficient funds.")
      }
    }

    case ActorAccount.Deposit(amt) => {
      if (amt + ActorAccount.accountBalance < ActorAccount.maxAccountBalance) {
        accountBalance += amt
        println(s"The balance deposited is $amt and the remaining balance is " + ActorAccount.accountBalance)
      }
      else {
        println(s"The balance in your account is " + ActorAccount.accountBalance + s". It can't be deposited as it is exceeding maximum balance.")
      }
    }

    case ActorAccount.getDetails(accountId) => {
      println(s"Account Id: ${accountId}")
      println(s"Account Owner: ${accountOwnerName}")
      println(s"Account Owner Age: ${accountOwnerAge}")
      println(s"Account Owner Phone Number: ${accountOwnerPhone}")
      println(s"Account Owner Email: ${accountOwnerEmail}")
      println(s"Account Balance: $accountBalance")
      sender() ! Balance(accountBalance)
    }
  }
}
