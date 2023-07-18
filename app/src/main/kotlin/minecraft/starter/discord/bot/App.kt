package minecraft.starter.discord.bot
import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import net.dv8tion.jda.api.interactions.commands.build.Commands
import io.github.cdimascio.dotenv.dotenv
import kotlin.system.exitProcess

var ngrok_link = ""

class Bot : ListenerAdapter() {
    override fun onSlashCommandInteraction(event: SlashCommandInteractionEvent) {
        when (event.name) {
            "start" -> {
                println("start")
                event.reply("Not Yet Implemented").queue()
            }

            "stop" -> {
                println("stop")
                event.reply("Not Yet Implemented").queue()
            }

            "ip" -> {
                ipCommand(event)
            }
        }
    }
}

fun main() {
    val dotenv = dotenv {
        ignoreIfMalformed = true
        ignoreIfMissing = true
    }

    val token: String? = dotenv["DISCORD_TOKEN"]

    if (token.isNullOrBlank()) {
        println("Cannot find DISCORD_TOKEN!")
        exitProcess(1)
    }

    ngrok_link = dotenv["NGROK_WEB_URL"]

    val jda = JDABuilder.createDefault(token)
    jda.addEventListeners(Bot())
    val bot = jda.build()

    bot.updateCommands().addCommands(
        Commands.slash("ping", "Ping!"),
        Commands.slash("start","Start the Minecraft server"),
        Commands.slash("ip", "Fetches public server ip")
    ).queue()
}
