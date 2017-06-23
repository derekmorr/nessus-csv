import java.net.{InetAddress, URL}
import java.sql.Connection
import java.time.LocalDateTime

import Types.{CVENumber, MacAddress}
import anorm._
import eu.timepit.refined.types.net.PortNumber
import cats.syntax.eq._
import cats.instances.all._
import com.google.common.net.{InetAddresses, InternetDomainName}
import eu.timepit.refined.W
import eu.timepit.refined.api.Refined
import eu.timepit.refined.string.MatchesRegex
import refined.anorm._

object Types {
  type MacAddress = String Refined MatchesRegex[W.`"^([0-9A-Fa-f]{2}[:-]){5}([0-9A-Fa-f]{2})$"`.T]
  type CVENumber = String Refined MatchesRegex[W.`"^(CVE-[0-9]{4}-[0-9]*,??)*"`.T]

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
                  vulnPubDate: Option[LocalDateTime],
                  exploitEase: String
                  )

object Nessus {
  private val TEXT_LENGTH = 65535

  def insert(record: Nessus)(implicit connection: Connection): Boolean = {
    val urls = record.seeAlso.map(_.toExternalForm).mkString(System.lineSeparator())
    val ip = InetAddresses.toAddrString(record.ipAddress)
    val text = record.pluginText.take(TEXT_LENGTH)
    val description = record.description.take(TEXT_LENGTH)
    val solution = record.solution.take(TEXT_LENGTH)

    SQL"""INSERT INTO nessus (plugin, pluginName, severity, IPAddress, port, protocol, family, exploit, DNSname,
          NetBIOSname, pluginText, synopsis, description, solution, seeAlso, cve, firstDiscovered,
          lastObserved, exploitEase, exploitFrameworks, repository, MACaddress, vulnPublicationDate, importedDate) VALUES
          (${record.plugin}, ${record.pluginName}, ${record.severity}, $ip, ${record.port}, ${record.protocol},
           ${record.family}, ${record.exploit}, ${record.dnsName.toString}, ${record.netBiosName}, $text,
           ${record.synopsis}, $description, $solution, $urls, ${record.cve}, ${record.firstDiscovered},
           ${record.lastObserved}, ${record.exploitEase}, ${record.exploitFrameworks}, ${record.repository},
           ${record.macAddress}, ${record.vulnPubDate}, ${LocalDateTime.now()})"""
      .executeUpdate() === 1
  }
}
