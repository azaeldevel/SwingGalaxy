
package arm.galaxies.swing;

import java.awt.event.WindowEvent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import org.jdesktop.swingx.JXLoginPane;
import org.jdesktop.swingx.auth.LoginAdapter;
import org.jdesktop.swingx.auth.LoginEvent;
import org.jdesktop.swingx.auth.LoginListener;
import org.jdesktop.swingx.auth.LoginService;

/**
 *
 * @author Azael
 */
public class LoginView
{
    private String userName;
    private int failedAttemptsCount = 0;
    JFrame frame;
    JXLoginPane loginPane;
    JPanel tranfering;
    
    public LoginView(JFrame container, JPanel trans)
    {
        frame = container;
        tranfering = trans;
    }
    
    public void showLoginDialog() 
    {        
        final JXLoginPane loginPane = new JXLoginPane();

        LoginListener loginListener = new LoginAdapter() 
        {
            @Override
            public void loginFailed(LoginEvent source) 
            {
                failedAttemptsCount++;
                String message;
                switch(failedAttemptsCount) {
                    case 1: message = "Come on buddy! What happened?"; break;
                    case 2: message = "Did you just fail again?"; break;
                    case 3: message = "This is embarrassing..."; break;
                        default: message = "You should probably go home and get some sleep...";
                }
                loginPane.setErrorMessage(message);
            }

            @Override
            public void loginSucceeded(LoginEvent source) 
            {
                userName = loginPane.getUserName();
                createAndShowGui();
            }
        };

        LoginService loginService = new LoginService() 
        {
            @Override
            public boolean authenticate(String name, char[] password, String server) throws Exception 
            {
                return name.equals("admin") && String.valueOf(password).equals("admin") ;
            }
        };

        loginService.addLoginListener(loginListener);
        loginPane.setLoginService(loginService);


        JXLoginPane.JXLoginDialog dialog = new JXLoginPane.JXLoginDialog(frame, loginPane);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setVisible(true);

        // if loginPane was cancelled or closed then its status is CANCELLED
        // and still need to dispose main JFrame to exiting application
        if(loginPane.getStatus() == JXLoginPane.Status.CANCELLED) 
        {
            frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
        }
    }

    private void createAndShowGui() 
    {
        frame.setSize(tranfering.getSize());
        frame.setContentPane(tranfering);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }    
}
