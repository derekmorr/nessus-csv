import java.net.{InetAddress, URL}
import java.time.LocalDateTime

case class Nessus(plugin: Int,
                  pluginName: String,
                  family: String,
                  severity: String,
                  ipAddress: InetAddress,
                  protocol: String,
                  port: PortNumber,
                  exploit: Boolean,
                  repository: String,
                  macAddress: String,
                  dnsName: String,
                  netBiosName: String,
                  pluginText: String,
                  firstDiscovered: Option[LocalDateTime],
                  lastObserved: Option[LocalDateTime],
                  exploitFrameworks: String,
                  synopsis: String,
                  description: String,
                  solution: String,
                  seeAlso: List[URL],
                  cve: String,
                  vulnPubDate: Option[LocalDateTime],
                  exploitEase: String
                  )
