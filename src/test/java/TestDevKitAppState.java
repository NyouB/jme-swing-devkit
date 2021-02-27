import fr.exratio.jme.devkit.appstate.annotations.BooleanProperty;
import fr.exratio.jme.devkit.appstate.annotations.CustomComponent;
import fr.exratio.jme.devkit.appstate.annotations.DevKitAppState;
import fr.exratio.jme.devkit.appstate.annotations.EnumProperty;
import fr.exratio.jme.devkit.appstate.annotations.FloatProperty;
import fr.exratio.jme.devkit.appstate.annotations.IntegerProperty;
import fr.exratio.jme.devkit.appstate.annotations.ListProperty;
import fr.exratio.jme.devkit.appstate.annotations.ListType;
import javax.swing.JButton;
import javax.swing.JComponent;

// Add the DevKit annotation so the DevKit will add it to the list of available AppStates to run.
@DevKitAppState(tabs = {"Tab 1", "Tab 2", "Tab 3"})
public class TestDevKitAppState {

  private final String[] customList = {"String 1", "String 2", "String 3", "String 4"};
  private float myFloatValue = 20.0f;
  private int myIntValue = 5;
  private TestEnum myTestEnum = TestEnum.Value_2;
  private int customListIndex = 2;
  private boolean myBooleanValue = false;

  // required: no-args constructor
  public TestDevKitAppState() {

  }

  // Creates a slider with the specified ranges.
  @FloatProperty(min = 4.0f, max = 100.0f, step = 2.0f)
  public float getMyFloatValue() {
    return myFloatValue;
  }

  public void setMyFloatValue(float myFloatValue) {
    this.myFloatValue = myFloatValue;
  }

  // Creates a slider with the specified ranges.
  @IntegerProperty(min = -10, max = 135, step = 5, tab = "Tab 1")
  public int getMyIntValue() {
    return myIntValue;
  }

  public void setMyIntValue(int myIntValue) {
    this.myIntValue = myIntValue;
  }

  // Creates a combobox.
  @EnumProperty(tab = "Tab 2")
  public TestEnum getMyTestEnum() {
    return myTestEnum;
  }

  public void setMyTestEnum(TestEnum myTestEnum) {
    this.myTestEnum = myTestEnum;
  }

  // Creates a list or combobox from the given array.
  @ListProperty(accessorName = "CustomListIndex", listType = ListType.ComboBox, tab = "Tab 3")
  public String[] getMyCustomListValues() {
    return customList;
  }

  public String[] getCustomList() {
    return customList;
  }

  public int getCustomListIndex() {
    return customListIndex;
  }

  public void setCustomListIndex(int customListIndex) {
    this.customListIndex = customListIndex;
  }

  // Adds the custom component to the GUI.
  @CustomComponent
  public JComponent getCustomComponent() {
    return new JButton("Custom Buttom");
  }

  @BooleanProperty
  public boolean isMyBooleanValue() {
    return myBooleanValue;
  }

  // Adds a checkbox

  public void setMyBooleanValue(boolean myBooleanValue) {
    this.myBooleanValue = myBooleanValue;
  }

  public enum TestEnum {Value_1, Value_2, Value_3}
}
