package core

class FormatRecognizer {

  def parseToList(text : String) : (Char, List[String]) = {
    var parsedString : (Char, List[String])= (' ', List(""))

    val separators : List[(Char, Int)] = text.filter(!_.isLetterOrDigit).groupBy(c => c.toLower)
      .map(m => (m._1, m._2.length)).toList.sortBy(_._2).reverse

    if (separators.size > 0) {
      parsedString = (separators(0)._1 ,text.split(separators(0)._1).toList)
    }

    parsedString
  }
}
