package ui

import java.awt.datatransfer.{Clipboard, DataFlavor, StringSelection}
import java.awt.{Color, Toolkit}

import core.{FormatRecognizer, SortManager, Sortable}

import scala.swing._
import scala.swing.event._

object OgreFrame {
  private val copiedText = new Label
  private val sortedText = new Label
  private val format = new FormatRecognizer

  private val mainFrame = new MainFrame
  private val sorting : Sortable = new SortManager

  private val foregroundColor = Color.white
  private val backgroundColor = Color.darkGray

  def getFrame : MainFrame = {

      mainFrame.title = "OgreSort"
      mainFrame.preferredSize = new Dimension(500, 500)

      mainFrame.contents = sortPane

      mainFrame
  }

  private def sortPane = new FlowPanel {

    // 1 2 5 4 3 2 9 1 0 2
    name = "Main Panel"
    background = backgroundColor
    foreground = foregroundColor

    copiedText.preferredSize = new Dimension(mainFrame.preferredSize.width, 20)
    sortedText.preferredSize = new Dimension(mainFrame.preferredSize.width, 20)

    copiedText.foreground = foregroundColor
    sortedText.foreground = Color.orange

    val copyPanel = borderPanel("Display")
    copyPanel.preferredSize = new Dimension( (mainFrame.preferredSize.width * 0.5).toInt, (mainFrame.preferredSize.height * 0.4).toInt)
    copyPanel.border = Swing.LineBorder(Color.gray)

    val clipPanel = borderPanel("Copy directly to Clipboard")
    clipPanel.preferredSize = copyPanel.preferredSize
    clipPanel.border = Swing.LineBorder(Color.gray)

    val purge = purgePanel
    purge.border = Swing.LineBorder(Color.gray)

    contents += copiedText
    contents += sortedText
    contents += new BoxPanel(Orientation.Horizontal) {
      contents += copyPanel
      contents += clipPanel
    }
    contents += new BoxPanel(Orientation.Vertical) {
      contents += purge
    }

  }

  private def purgePanel () = new BorderPanel {
    background = backgroundColor
    foreground = foregroundColor

    val purgBtn = createStandardButton("Purge")
    purgBtn.preferredSize = new Dimension(200, 50)
    purgBtn.reactions += handlePurging

    val lblTitle = new Label {
      text = "Purge Clipboard"
      foreground = foregroundColor
      preferredSize = new Dimension(60, 30)
    }

    add(lblTitle, BorderPanel.Position.North)
    add(new BoxPanel(Orientation.Vertical) {
      background = backgroundColor
      foreground = foregroundColor
      border = Swing.EmptyBorder(50,100,0,0)
      contents += purgBtn
    }, BorderPanel.Position.Center)

  }
  private def borderPanel (title: String) = new BorderPanel {

    background = backgroundColor
    foreground = foregroundColor

    val ascBtn = createStandardButton("ASC")
    ascBtn.preferredSize = new Dimension(200, 50)
    ascBtn.reactions += handleSorting("ASC")

    val descBtn = createStandardButton("DESC")
    descBtn.preferredSize = new Dimension(200, 50)
    descBtn.reactions += handleSorting("DESC")

    val lblTitle = new Label {
      text = title
      foreground = foregroundColor
      preferredSize = new Dimension(60, 30)
    }

    add(lblTitle, BorderPanel.Position.North)
    add(new BoxPanel(Orientation.Vertical) {
      background = backgroundColor
      foreground = foregroundColor
      border = Swing.EmptyBorder(50,100,0,0)
      contents += ascBtn
      contents += descBtn
    }, BorderPanel.Position.Center)

  }

  private def createStandardButton (title : String) = new Button {

    text = title
    background = backgroundColor
    foreground = foregroundColor
    borderPainted = true
    focusable = false
    listenTo(mouse.clicks)
  }

  private def handleSorting (title: String) : Reactions.Reaction = {
    case clicked: MouseClicked => {
      val button: Int = clicked.peer.getButton()

      if (button > 1) {
        copiedText.text = getClipBoardContent()

        val formatted: (Char, List[String]) = format.parseToList(copiedText.text)

        val sorted : String = sorting.sort(formatted)
        sortedText.text = if (title == "ASC") sorted else sorted.reverse
      }
    }
  }

  private def handlePurging () : Reactions.Reaction = {
    case clicked: MouseClicked => {
      val button: Int = clicked.peer.getButton()

      if (button > 1) {
        setClipBoardContent("")
      }
    }
  }




  private def getClipBoardContent () : String = {
    try {
      val clipBoard : Clipboard = Toolkit.getDefaultToolkit().getSystemClipboard()

      val stuff : AnyRef = clipBoard.getContents(null).getTransferData(DataFlavor.stringFlavor)

      stuff.toString()
    }
    catch {
      case anyThing : Exception => {
        "what"
      }

    }
  }

  private def setClipBoardContent (content : String) : Unit = {
    try {
      val clipBoard : Clipboard = Toolkit.getDefaultToolkit().getSystemClipboard()

      val selection = new StringSelection(content)
      clipBoard.setContents(selection, selection)
    }
    catch {
      case anyExc : Exception => {

      }
    }
  }

}
