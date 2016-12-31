package nl.jvdploeg.flat.job;

public class NopJob extends DefaultJob {

  public NopJob() {
  }

  @Override
  public String toString() {
    return "NopJob";
  }

  @Override
  protected void executeImpl() {
    // no operation
  }
}
