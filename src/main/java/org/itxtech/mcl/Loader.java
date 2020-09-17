package org.itxtech.mcl;

import org.apache.commons.cli.*;
import org.itxtech.mcl.component.Config;
import org.itxtech.mcl.component.Downloader;
import org.itxtech.mcl.component.Logger;
import org.itxtech.mcl.component.MiraiRepo;
import org.itxtech.mcl.impl.DefaultDownloader;
import org.itxtech.mcl.impl.DefaultLogger;
import org.itxtech.mcl.script.ScriptManager;

import java.io.File;

/*
 *
 * Mirai Console Loader
 *
 * Copyright (C) 2020 iTX Technologies
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * @author PeratX
 * @website https://github.com/iTXTech/mirai-console-loader
 *
 */
public class Loader {
    public Downloader downloader = new DefaultDownloader();
    public Logger logger = new DefaultLogger();
    public File configFile = new File("config.json");
    public Config config;
    public ScriptManager manager;
    public MiraiRepo repo;
    public Options options = new Options();
    public CommandLine cli;

    public static void main(String[] args) {
        new Loader().start(args);
    }

    /**
     * 启动 Mirai Console Loader，并加载脚本
     */
    public void start(String[] args) {
        logger.info("Mirai Console Loader by iTX Technologies");
        logger.info("https://github.com/iTXTech/mirai-console-loader");
        logger.info("Licensed under AGPLv3");


        try {
            config = Config.load(configFile);
            repo = new MiraiRepo(config.miraiRepo);
            manager = new ScriptManager(this, new File("scripts"));
            manager.readAllScripts();
            manager.phaseCli();
            try {
                cli = new DefaultParser().parse(options, args);
            } catch (ParseException e) {
                logger.error(e.getMessage());
                new HelpFormatter().printHelp("mcl", options);
                System.exit(1);
            }
            config.save(configFile);
        } catch (Throwable e) {
            logger.logException(e);
        }
    }
}