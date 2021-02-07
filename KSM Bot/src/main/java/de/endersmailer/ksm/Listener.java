package de.endersmailer.ksm;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.managers.ChannelManager;
import org.jetbrains.annotations.NotNull;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Random;

public class Listener extends ListenerAdapter {

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent ev){
        //new Thread(() -> {
        if (ev.getMember().getUser().isBot()){
            return;
        }
        if (ev.getChannel().getId().equalsIgnoreCase("807629547222728734")){
            Guild guild = ev.getGuild();
            //ev.getMember()
            Role registriertRole = guild.getRoles().stream()
                    .filter(role -> role.getIdLong() == Long.parseLong("807417217750794240"))
                    .findFirst()
                    .orElse(null);

            if (registriertRole == null) {
                ev.getChannel().sendMessage("Sorry aber irgendwas ist schief gelaufen, schreibe bitte Simon Blackblood an! Error:#0002").queue();
                return;
            }
            guild.addRoleToMember(ev.getMember(), registriertRole).queue();
            guild.modifyNickname(ev.getMember(), ev.getMessage().getContentStripped()).queue();
        }

        //}).start();
    }
    @Override
    public void onMessageReactionAdd(@NotNull  MessageReactionAddEvent ev){
        if (ev.getUser().isBot()){
            return;
        }

        new Thread(() -> {
            JDA jda = ev.getJDA();
            Guild guild = ev.getGuild();
            if (ev.getChannelType() == ChannelType.TEXT){
                if (ev.getChannel().getIdLong() == Long.parseLong("807603220939014164")){
                    Category termineCategory = guild.getCategories().stream()
                            .filter(category -> category.getName().equalsIgnoreCase("Termine"))
                            .findFirst()
                            .orElse(null);

                    if (termineCategory == null){
                        guild.getOwner().getUser().openPrivateChannel().queue(ch ->
                                ch.sendMessage("Channel ERROR: #003! " + ev.getUser().getName() + " | " + ev.getUser().getAsTag()).queue()
                        );
                    }else{
                        ev.getTextChannel().removeReactionById(ev.getMessageId(),ev.getReactionEmote().getAsReactionCode(), ev.getUser()).queue();
                        String name = getName("termin-", termineCategory);

                        EmbedBuilder eb = new EmbedBuilder();
                        eb.setAuthor("KSM", "https://discord.com/invite/wKN3pkE",null);
                        eb.addField("Terminvereinbarung","Bitte Schreibe uns was du haben möchtest ( Große Türen | Aufzug ) und wann du so täglich in der Stadt bist. \n \n Wenn du fertig bist, reagiere bitte auf diese Nachricht!",false);
                        eb.setFooter("By ViaEnder");
                        eb.setTimestamp(Instant.now());


                        termineCategory.createTextChannel(name).queue(textChannel -> {
                            textChannel.createPermissionOverride(ev.getMember()).setAllow(
                                    Permission.VIEW_CHANNEL,
                                    Permission.MESSAGE_READ,
                                    Permission.MESSAGE_WRITE,
                                    Permission.MESSAGE_HISTORY
                            ).queue();
                            textChannel.sendMessage(eb.build()).queue(message -> message.addReaction("✉").queue());
                        });
                    }
                }
                else if (ev.getChannel().getName().startsWith("termin-")) {
                    if (ev.getReactionEmote().getAsReactionCode().equalsIgnoreCase("✉")){
                        ev.getTextChannel().removeReactionById(ev.getMessageId(), ev.getReactionEmote().getAsReactionCode(), ev.getUser()).queue();
                        ev.getTextChannel().addReactionById(ev.getMessageId(), "❌").queue(unused ->
                                ev.getTextChannel().addReactionById(ev.getMessageId(), "✅").queue());


                    }else if (ev.getReactionEmote().getAsReactionCode().equalsIgnoreCase("✅")){
                        ev.getTextChannel().removeReactionById(ev.getMessageId(), ev.getReactionEmote().getAsReactionCode(), ev.getUser()).queue();
                        String name = "g-ticket-" + ev.getTextChannel().getName().replace("termin-", "");
                        Category c = ev.getGuild().getCategories().stream()
                                .filter(category -> category.getId().equalsIgnoreCase("807950386065178654"))
                                .findFirst()
                                .orElse(null);

                        ev.getTextChannel().createPermissionOverride(ev.getMember()).setDeny(
                                Permission.VIEW_CHANNEL,
                                Permission.MESSAGE_READ,
                                Permission.MESSAGE_WRITE,
                                Permission.MESSAGE_HISTORY
                        ).queue();

                        ChannelManager channel = ev.getTextChannel().getManager();
                        channel.setParent(c).queue();
                        channel.setName(name).queue();

                    }else if (ev.getReactionEmote().getAsReactionCode().equalsIgnoreCase("✅")){
                        ev.getTextChannel().removeReactionById(ev.getMessageId(), ev.getReactionEmote().getAsReactionCode(), ev.getUser()).queue();
                        ev.getTextChannel().removeReactionById(ev.getMessageId(), "❌", jda.getSelfUser()).queue();
                        ev.getTextChannel().removeReactionById(ev.getMessageId(), "️️✅", jda.getSelfUser()).queue();
                    }
                }
                else if (ev.getChannel().getIdLong() == Long.parseLong("808002722380906541")){
                    Category category = guild.getCategories().stream()
                            .filter(category1 -> category1.getId().equalsIgnoreCase("808012385977499660"))
                            .findFirst()
                            .orElse(null);
                    if (category != null) {
                        ev.getTextChannel().removeReactionById(ev.getMessageId(), ev.getReactionEmote().getAsReactionCode(), ev.getUser()).queue();
                        String name = getName("bewerbung-", category);

                        EmbedBuilder eb = new EmbedBuilder();
                        eb.setAuthor("KSM", "https://discord.com/invite/wKN3pkE",null);
                        eb.addField("Bewerbungsschreiben","Schreibe bitte nach dieser Nachricht deine Bewerbung. \n \n \n Wenn du fertig bist, reagiere bitte auf diese Nachricht!",false);
                        eb.setFooter("By ViaEnder");
                        eb.setTimestamp(Instant.now());

                        category.createTextChannel(name).queue(textChannel -> {
                            textChannel.createPermissionOverride(ev.getMember()).setAllow(
                                    Permission.VIEW_CHANNEL,
                                    Permission.MESSAGE_READ,
                                    Permission.MESSAGE_WRITE,
                                    Permission.MESSAGE_HISTORY
                            ).queue();
                            textChannel.sendMessage(eb.build()).queue(message -> message.addReaction("✉").queue());
                        });

                    }else{
                        System.out.println("Category Nicht gefunden!");
                    }
                }

            }
        }).start();
    }


    public static String getName(String prefix, Category category){
        String name = prefix + getRandomNum();
        TextChannel tc = category.getTextChannels().stream()
                .filter(guildChannel -> guildChannel.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
        if (tc == null){
            return name;
        }else{
            return getName(prefix, category);
        }
    }

    public static int getRandomNum(){
        return new Random().nextInt((9999 - 1000) +1) + 1000;
    }

    public Role findRole(Member member, String roleName){
        List<Role> roles = member.getRoles();
        return roles.stream()
                .filter(role -> role.getName().equalsIgnoreCase(roleName))
                .findFirst()
                .orElse(null);
    }
}
