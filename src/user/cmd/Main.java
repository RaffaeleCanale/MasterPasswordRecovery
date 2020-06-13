package user.cmd;

import com.wx.action.cmd.CommandContext;
import com.wx.console.Console;
import com.wx.console.UserConsoleInterface;
import com.wx.console.system.SystemConsole;
import com.wx.console.system.UnixSystemConsole;
import com.wx.grammar.ParseException;
import com.wx.util.OsUtils;
import com.wx.util.log.LogHelper;
import user.cmd.list.Decrypt;
import user.cmd.list.Encrypt;
import user.cmd.list.Generate;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created on 23/08/2015
 *
 * @author Raffaele Canale (raffaelecanale@gmail.com)
 * @version 0.1
 */
public class Main {

    public static final String ENCRYPTED_EXTENSION = ".wxe";
    public static final UserConsoleInterface IN = new UserConsoleInterface(initConsole(), "", ">");
    public static final double VERSION = 1.5;

    public static final Logger LOG = initLogger();

    private static Logger initLogger() {
        LogHelper.setupLogger(LogHelper.consoleHandlerShort(Level.ALL));
        return LogHelper.getLogger(Main.class);
    }

    public static void disableLogger() {
        LOG.setLevel(Level.OFF);
    }


    private static Console initConsole() {
        switch (OsUtils.getOsFamily()) {
            case UNIX:
                return new UnixSystemConsole();
            default:
                return new SystemConsole();
        }
    }

    public static void main(String[] args) {
        try {
            CommandContext context = new CommandContext.Builder()
                    .addHelpCommand()
                    .addExitCommand()
                    .loadDescriptionsFrom("/Commands.sp", ENCRYPTED_EXTENSION)
                    .attachAction("encrypt", new Encrypt())
                    .attachAction("decrypt", new Decrypt())
                    .attachAction("generate", new Generate())
                    .build(IN);

            context.runOrExecute(args);
        } catch (IOException | ParseException e) {
            IN.getConsole().errln(e.getClass().getSimpleName() + ": " + e.getMessage());
            LOG.log(Level.SEVERE, "", e);
        }
    }

}
