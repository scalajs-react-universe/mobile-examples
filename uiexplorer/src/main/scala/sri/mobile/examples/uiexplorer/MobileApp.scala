package sri.mobile.examples.uiexplorer

import sri.mobile.examples.uiexplorer.components.{
  UIExplorerDetailsScreen,
  UIExplorerListScreen
}
import sri.navigation._
import sri.navigation.navigators._
import sri.universal.apis.AppRegistry

object MobileApp {

  def main(args: Array[String]) = {
    val root = StackNavigator(
      registerStackScreen[UIExplorerListScreen](navigationOptions =
        NavigationStackScreenOptions(title = "UIExplorer")),
      registerStackScreen[UIExplorerDetailsScreen](
        navigationOptionsDynamic =
          (props: NavigationScreenConfigProps[UIExplorerDetailsScreen]) =>
            NavigationStackScreenOptions(
              title = props.navigation.state.params.get.title))
    )
    AppRegistry.registerComponent("UIExplorer", () => root)
  }
}
