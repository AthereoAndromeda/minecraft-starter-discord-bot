package minecraft.starter.discord.bot
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.ObjectMapper
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import org.http4k.client.JavaHttpClient
import org.http4k.core.HttpHandler
import org.http4k.core.Method
import org.http4k.core.Request

data class Metrics(
    @JsonProperty("conns") val connections: ConnectionMetrics,
    @JsonProperty("http") val http: HttpMetrics
)

data class ConnectionMetrics(
    @JsonProperty("count") val count: Int,
    @JsonProperty("gauge") val gauge: Int,
    @JsonProperty("p50") val p50: Long,
    @JsonProperty("p90") val p90: Double,
    @JsonProperty("p95") val p95: Double,
    @JsonProperty("p99") val p99: Long,
    @JsonProperty("rate1") val rate1: Double,
    @JsonProperty("rate15") val rate15: Double,
    @JsonProperty("rate5") val rate5: Double
)

data class HttpMetrics(
    @JsonProperty("count") val count: Int,
    @JsonProperty("p50") val p50: Int,
    @JsonProperty("p90") val p90: Int,
    @JsonProperty("p95") val p95: Int,
    @JsonProperty("p99") val p99: Int,
    @JsonProperty("rate1") val rate1: Int,
    @JsonProperty("rate15") val rate15: Int,
    @JsonProperty("rate5") val rate5: Int
)

data class NgrokData(
    @JsonProperty("ID") val id: String,
    @JsonProperty("config") val config: Config,
    @JsonProperty("metrics") val metrics: Metrics,
    @JsonProperty("name") val name: String,
    @JsonProperty("proto") val proto: String,
    @JsonProperty("public_url") val publicUrl: String,
    @JsonProperty("uri") val uri: String
)

data class Config(
    @JsonProperty("addr") val addr: String,
    @JsonProperty("inspect") val inspect: Boolean
)

fun fetchNgrokData(): NgrokData {
    val request = Request(Method.GET, ngrok_link)
    val client: HttpHandler = JavaHttpClient()

    val response = client(request)
    val objectMapper = ObjectMapper()
    return objectMapper.readValue(response.bodyString(), NgrokData::class.java)
}

fun ipCommand(event: SlashCommandInteractionEvent) {
    val data = fetchNgrokData()

    // Substring is to remove the leading `tcp://` from string
    event.reply(data.publicUrl.substring(6)).queue()
}