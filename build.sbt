//import org.openqa.selenium.chrome.ChromeOptions

name := "example-bad-callback"

version := "0.1"

scalaVersion := "2.13.3"

enablePlugins(ScalaJSPlugin)

libraryDependencies += "org.scalatest" %%% "scalatest" % "3.2.5" % Test
//
//jsEnv in Test := new org.scalajs.jsenv.selenium.SeleniumJSEnv(
//  new ChromeOptions,
//  //SeleniumJSEnv.Config().withKeepAlive(true)
//)