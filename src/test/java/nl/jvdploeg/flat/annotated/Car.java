package nl.jvdploeg.flat.annotated;

@Node(path = "Cars/{id}", value = "value", children = { "lane" })
public class Car {

  private String id;
  private String lane;
  private String value;

  public Car(Car reference, String value, String lane) {
    this(reference.id, value, lane);
  }

  public Car(String id) {
    this.id = id;
  }

  public Car(String id, String value, String lane) {
    this.id = id;
    this.value = value;
    this.lane = lane;
  }

  public String getId() {
    return id;
  }

  public String getLane() {
    return lane;
  }

  public String getValue() {
    return value;
  }
}
