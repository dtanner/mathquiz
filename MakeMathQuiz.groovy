import groovy.xml.MarkupBuilder

class MakeMathQuiz {

    // todo - don't page break the last table

    // editable values

    def number_of_rows = 8
    def number_of_columns = 5
    def highest_number_to_use_in_test = 20
    def number_of_tests_to_create = 2
    def type_of_test = "add" // one of add, subtract, multiply, divide

    // you don't need to change anything below this

    Random random = new Random()

    static void main(args) {
        MakeMathQuiz test = new MakeMathQuiz()
        if (args) {
            test.type_of_test = Integer.parseInt(args[0])
        }

        test.createHtmlFile("math-test.html")
    }


    int getRandomNumber(int maxValue) {
        // any number between 1 and the maximum value
        random.nextInt(maxValue + 1)
    }

    String buildCell() {
        if (type_of_test == "add") return buildAdditionCell()
        if (type_of_test == "subtract") return buildSubtractionCell()
        if (type_of_test == "multiply") return buildMultiplicationCell()
        if (type_of_test == "divide") return buildDivisionCell()
        throw new RuntimeException("I don't know what type of cell to build.")
    }

    String buildAdditionCell() {
        """${getRandomNumber(highest_number_to_use_in_test)} <br>
       + ${getRandomNumber(highest_number_to_use_in_test)} <br>
      <hr width=40px>
    """
    }

    String buildSubtractionCell() {
        int topValue = getRandomNumber(highest_number_to_use_in_test)
        """${topValue} <br>
           - ${getRandomNumber(topValue)} <br>
          <hr width=40px>
        """
    }

    String buildDivisionCell() {
        int topValue = getRandomNumber(highest_number_to_use_in_test)
        """${topValue} <br>
           &divide; ${getRandomNumber(topValue)} <br>
          <hr width=40px>
        """
    }

    String buildMultiplicationCell() {
        """${getRandomNumber(highest_number_to_use_in_test)} <br>
       x ${getRandomNumber(highest_number_to_use_in_test)} <br>
      <hr width=40px>
      """
    }

    void createHtmlFile(String fileName) {
        def writer = new StringWriter()
        def builder = new MarkupBuilder(writer)
        builder.html() {
            head {
                style("@media print { table {page-break-after:always} }")
            }
            body {
                number_of_tests_to_create.times {
                    table([align: 'center']) {
                        number_of_rows.times {
                            tr() {
                                number_of_columns.times {
                                    td([align: 'right', style: 'font-size:22px; padding-left:30px; padding-right:30px;padding-bottom:30px'], { mkp.yieldUnescaped(buildCell()) })
                                }
                            }
                        }
                    }
                }
            }
        }

        def f = new File(fileName)
        f.delete()
        f << writer.toString()
    }

}

