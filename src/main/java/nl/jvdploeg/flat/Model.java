package nl.jvdploeg.flat;

public class Model {

  private final Enforcement enforcement;
  private final String name;
  private final ModelPublisher publisher;
  private final Node root;

  /**
   * Create a model.
   *
   * @param name
   *          The name of the model.
   * @param enforcement
   *          The node creation strategy.
   */
  public Model(final String name, final Enforcement enforcement) {
    this.name = name;
    this.enforcement = enforcement;
    root = new Node();
    publisher = new ModelPublisher(this);
  }

  /**
   * Create a model by cloning another.
   *
   * @param name
   *          The name of the model.
   * @param enforcement
   *          The node creation strategy.
   */
  public Model(final String name, final Enforcement enforcement, final Model model) {
    this.name = name;
    this.enforcement = enforcement;
    root = new Node(model.root);
    publisher = new ModelPublisher(this);
  }

  /**
   * Follow the path and add (create) the last node in the path.
   *
   * @param path
   *          The path.
   */
  public void add(final Path path) {
    NodeUtils.addLast(root, path, enforcement);
    publisher.publishNext(DefaultChange.add(path));
  }

  public String getName() {
    return name;
  }

  /**
   * Follow the path and get the last node in the path.
   *
   * @param path
   *          The path.
   * @return The last node in the path.
   */
  public Node getNode(final Path path) {
    final Node node = NodeUtils.getNode(root, path);
    return node;
  }

  public ModelPublisher getPublisher() {
    return publisher;
  }

  public Node getRoot() {
    return root;
  }

  /**
   * Follow the path and get the value of the last node in the path.
   *
   * @param path
   *          The path.
   * @return The value.
   */
  public String getValue(final Path path) {
    final Node node = NodeUtils.getNode(root, path);
    final String value = node.getValue();
    return value;
  }

  /**
   * Follow the path and remove the last node in the path.
   *
   * @param path
   *          The path.
   */
  public void remove(final Path path) {
    NodeUtils.removeLast(root, path);
    publisher.publishNext(DefaultChange.remove(path));
  }

  /**
   * Follow the path and set the value of the last node in the path.
   *
   * @param path
   *          The path.
   * @param newValue
   *          The new value.
   */
  public void setValue(final Path path, final String newValue) {
    final Node node = NodeUtils.getNode(root, path);
    final String oldValue = node.setValue(newValue);
    publisher.publishNext(DefaultChange.set(path, oldValue, newValue));
  }

  @Override
  public String toString() {
    return "[" + name + ", " + enforcement + ", " + root + "]";
  }
}
