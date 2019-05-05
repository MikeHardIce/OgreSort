package core

trait Sortable {
  def sort(content : (Char, List[String])) : String
    = (content._2.sorted mkString content._1.toString).toString
}
