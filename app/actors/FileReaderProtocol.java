package actors;

public class FileReaderProtocol implements java.io.Serializable {
  public final String filename;
  public FileReaderProtocol(String filename) { this.filename = filename; }
}