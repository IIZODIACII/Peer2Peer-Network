public class RandomString {
   public String letters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvxyz";
   public String fake()
    {
      String fake_str="";
      int n =(int) (20*Math.random());
      n+=10;
      for(int i=0;i<n;i++)
      {
          fake_str+=letters.charAt((int)(letters.length()* Math.random()));
      }
      return fake_str;
    }
}
