package Client;

import javax.swing.*;
import javax.swing.border.*;

import Server.ServerInterface;

import java.awt.*;
import java.awt.event.*;
import java.rmi.Naming;
import java.util.*;

public class ClientMain
{
   private client client;
   private ServerInterface server;

   public void doConnect()
   {
      if (connect.getText().equals("Connect"))
      {
         if (name.getText().length() < 2)
         {
            JOptionPane.showMessageDialog(frame, "You need to type a name.");
            return;
         }
         if (ip.getText().length() < 2)
         {
            JOptionPane.showMessageDialog(frame, "You need to type an IP.");
            return;
         }
         try
         {
            client = new client(name.getText());
            client.setGUI(this);
            server = (ServerInterface) Naming.lookup("rmi://" + ip.getText()
                  + "/myabc");
            server.login(client);
            updateUsers(server.getConnected());
            connect.setText("Disconnect");
         }
         catch (Exception e)
         {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame,
                  "ERROR, we wouldn't connect....");
         }
      }
      else
      {
         updateUsers(null);
         connect.setText("Connect");
         // Better to implement Logout ....
      }
   }

   public void sendText()
   {
      if (connect.getText().equals("Connect"))
      {
         JOptionPane.showMessageDialog(frame, "You need to connect first.");
         return;
      }
      String st = tf.getText();
      st = "[" + name.getText() + "] " + st;
      tf.setText("");
      // Remove if you are going to implement for remote invocation
      try
      {
         server.publish(st);
      }
      catch (Exception e)
      {
         e.printStackTrace();
      }
   }

   public void writeMsg(String st)
   {
      tx.setText(tx.getText() + "\n" + st);
   }

   public void updateUsers(ArrayList arrayList)
   {
      DefaultListModel listModel = new DefaultListModel();
      if (arrayList != null)
         for (int i = 0; i < arrayList.size(); i++)
         {
            try
            {
               String tmp = ((ClientInterface) arrayList.get(i)).getName();
               listModel.addElement(tmp);
            }
            catch (Exception e)
            {
               e.printStackTrace();
            }
         }
      lst.setModel(listModel);
   }

   public static void main(String[] args)
   {
      System.out.println("Hello World !");
      ClientMain c = new ClientMain();
   }

   // User Interface code.
   public ClientMain()
   {
      frame = new JFrame("Group Chat");
      JPanel main = new JPanel();
      JPanel top = new JPanel();
      JPanel cn = new JPanel();
      JPanel bottom = new JPanel();
      ip = new JTextField();
      tf = new JTextField();
      name = new JTextField();
      tx = new JTextArea();
      connect = new JButton("Connect");
      JButton bt = new JButton("Send");
      lst = new JList();
      main.setLayout(new BorderLayout(5, 5));
      top.setLayout(new GridLayout(1, 0, 5, 5));
      cn.setLayout(new BorderLayout(5, 5));
      bottom.setLayout(new BorderLayout(5, 5));
      top.add(new JLabel("Your name: "));
      top.add(name);
      top.add(new JLabel("Server Address: "));
      top.add(ip);
      top.add(connect);
      cn.add(new JScrollPane(tx), BorderLayout.CENTER);
      cn.add(lst, BorderLayout.EAST);
      bottom.add(tf, BorderLayout.CENTER);
      bottom.add(bt, BorderLayout.EAST);
      main.add(top, BorderLayout.NORTH);
      main.add(cn, BorderLayout.CENTER);
      main.add(bottom, BorderLayout.SOUTH);
      main.setBorder(new EmptyBorder(10, 10, 10, 10));
      // Events
      connect.addActionListener(new ActionListener()
      {
         public void actionPerformed(ActionEvent e)
         {
            doConnect();
         }
      });
      bt.addActionListener(new ActionListener()
      {
         public void actionPerformed(ActionEvent e)
         {
            sendText();
         }
      });
      tf.addActionListener(new ActionListener()
      {
         public void actionPerformed(ActionEvent e)
         {
            sendText();
         }
      });

      frame.setContentPane(main);
      frame.setSize(600, 600);
      frame.setVisible(true);
   }

   JTextArea tx;
   JTextField tf, ip, name;
   JButton connect;
   JList lst;
   JFrame frame;

}