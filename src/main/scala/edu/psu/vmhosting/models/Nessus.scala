package edu.psu.vmhosting.models

import java.net.{Inet4Address, InetAddress, URL}
import java.sql.Connection
import java.time.LocalDateTime

import scala.concurrent.blocking

import anorm._
import Types._
import cats.syntax.eq._
import cats.instances.all._
import com.google.common.net.InternetDomainName
import edu.psu.vmhosting.anorm.AnormExtension._
import edu.psu.vmhosting.models.Types.{CVENumber, MacAddress}
import eu.timepit.refined.W
import eu.timepit.refined.api.Refined
import eu.timepit.refined.string.MatchesRegex
import eu.timepit.refined.types.net.PortNumber
import eu.timepit.refined.types.numeric.PosInt
import refined.anorm._

object Types {
  type MacAddress = String Refined MatchesRegex[W.`"^([0-9A-Fa-f]{2}[:-]){5}([0-9A-Fa-f]{2})$"`.T]
  type CVENumber = String Refined MatchesRegex[W.`"^(CVE-[0-9]{4}-[0-9]*,?)*"`.T]
}

case class Nessus(plugin: Int,
                  pluginName: String,
                  family: String,
                  severity: String,
                  ipAddress: InetAddress,
                  protocol: String,
                  port: PortNumber,
                  exploit: Boolean,
                  repository: String,
                  macAddress: Option[MacAddress],
                  dnsName: Option[InternetDomainName],
                  netBiosName: String,
                  pluginText: String,
                  firstDiscovered: Option[LocalDateTime],
                  lastObserved: Option[LocalDateTime],
                  exploitFrameworks: String,
                  synopsis: String,
                  description: String,
                  solution: String,
                  seeAlso: List[URL],
                  cve: Option[CVENumber],
                  vulnPublicationDate: Option[LocalDateTime],
                  exploitEase: String)

object Nessus {
  private val TEXT_LENGTH = 65535

  val parser: RowParser[Nessus] = Macro.namedParser[Nessus]

  def insert(record: Nessus)(implicit connection: Connection): Boolean = blocking {
    val urls = record.seeAlso.map(_.toExternalForm).mkString(System.lineSeparator())
    val text = record.pluginText.take(TEXT_LENGTH)
    val description = record.description.take(TEXT_LENGTH)
    val solution = record.solution.take(TEXT_LENGTH)

    SQL"""INSERT INTO nessus (plugin, pluginName, severity, IPAddress, port, protocol, family, exploit, DNSname,
          NetBIOSname, pluginText, synopsis, description, solution, seeAlso, cve, firstDiscovered,
          lastObserved, exploitEase, exploitFrameworks, repository, MACaddress, vulnPublicationDate, importedDate) VALUES
          (${record.plugin}, ${record.pluginName}, ${record.severity}, ${record.ipAddress}, ${record.port}, ${record.protocol},
           ${record.family}, ${record.exploit}, ${record.dnsName}, ${record.netBiosName}, $text,
           ${record.synopsis}, $description, $solution, $urls, ${record.cve}, ${record.firstDiscovered},
           ${record.lastObserved}, ${record.exploitEase}, ${record.exploitFrameworks}, ${record.repository},
           ${record.macAddress}, ${record.vulnPublicationDate}, ${LocalDateTime.now()})"""
      .executeUpdate() === 1
  }

  def take(n: PosInt)(implicit connection: Connection): List[Nessus] = {
    SQL"""SELECT * FROM nessus LIMIT ${n.value}"""
      .as(parser.*)
  }

  def forIp(inetAddress: InetAddress)(implicit connection: Connection): List[Nessus] = blocking {
    SQL"""SELECT * FROM nessus WHERE IPAddress = $inetAddress"""
      .as(parser.*)
  }
}
