package sri.mobile.examples.uiexplorer.components

import org.scalajs.dom
import sri.core._
import sri.universal._
import sri.universal.components._
import sri.universal.styles.InlineStyleSheetUniversal

import scala.scalajs.js
import scala.scalajs.js.{UndefOr => U}

object RefreshControlExample extends UIExample {

  object Row {
    val Component = (props: Props) =>
      TouchableWithoutFeedback(onPress = () => props.onClick(props.data))(
        View(style = styles.refreshControlRow)(
          Text(style = styles.text)(
            s"${props.data.text} (${props.data.clicks} clicks)")
        )
    )

    case class Props(data: RowData, onClick: RowData => _)

    def apply(data: RowData, onClick: RowData => _, key: String) =
      CreateElementSF(Component, Props(data, onClick), key)
  }

  case class RowData(text: String, clicks: Int, index: Int)

  case class State(
      isRefreshing: Boolean = false,
      loaded: Int = 0,
      rowData: Vector[RowData] = (1 until 20)
        .map(i => RowData(text = s"Initial row $i", clicks = 0, i))
        .toVector)

  class Component extends ComponentS[State] {

    initialState(State())

    def render() = {
      ScrollView(
        style = GlobalStyles.wholeContainer,
        refreshControl = RefreshControl(
          refreshing = state.isRefreshing,
          onRefresh = () => _onRefresh(),
          tintColor = "#ff0000",
          title = "Loading...",
          colors = js.Array("#ff0000", "#00ff00", "#0000ff"),
          progressBackgroundColor = "#ffff00"
        )
      )(
        state.rowData.zipWithIndex.map {
          case (rd, i) => Row(rd.copy(index = i), onRowClick _, i.toString)
        }
      )
    }

    def onRowClick(row: RowData) = {
      setState(
        (state: State) =>
          state.copy(rowData = state.rowData
            .updated(row.index, row.copy(clicks = row.clicks + 1))))
    }

    def _onRefresh() = {
      setState((state: State) => state.copy(isRefreshing = true))
      dom.window.setTimeout(
        () => {
          val rd = (1 until 10)
            .map(i => RowData(text = s"Loaded row $i", clicks = 0, i))
            .toVector
            .++(state.rowData)
          setState(
            (state: State) =>
              state.copy(loaded = state.loaded + 10,
                         isRefreshing = false,
                         rowData = rd))
        },
        5000
      )
    }
  }

  object styles extends InlineStyleSheetUniversal {

    import dsl._

    val refreshControlRow = style(
      borderColor := "grey",
      borderWidth := 1,
      padding := 20,
      backgroundColor := "#3a5795",
      margin := 5
    )
    val text = style(
      alignSelf.center,
      color := "#fff"
    )
    val scrollview = style(
      flex := 1
    )

  }

  val component = () => CreateElementNoProps[Component]()

  override def title: String = "RefreshControl"

  override def description: String =
    "Adds pull-to-refresh support to a scrollview."
}
