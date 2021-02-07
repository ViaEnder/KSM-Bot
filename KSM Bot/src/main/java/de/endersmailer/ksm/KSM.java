package de.endersmailer.ksm;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.utils.Compression;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

import javax.security.auth.login.LoginException;

public class KSM {

    public static JDA jda;

    public static void main(String[] args) throws LoginException, InterruptedException {
        JDABuilder builder = JDABuilder.createDefault("ODA3NjE4MzUyMDM3NjI1ODY2.YB6nMw.8xT6xjqxg44ivteAxntVsWIoatw");
        builder.disableCache(CacheFlag.MEMBER_OVERRIDES, CacheFlag.VOICE_STATE);
        builder.setBulkDeleteSplittingEnabled(false);
        builder.setCompression(Compression.NONE);
        builder.setActivity(Activity.watching("KSM Server"));
        jda = builder.build();

        jda.awaitReady();
        jda.addEventListener(new Listener());
    }


}
