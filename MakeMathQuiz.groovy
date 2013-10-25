import groovy.xml.MarkupBuilder

class MakeMathQuiz {

    // todo - don't page break the last table

    // editable values

    def number_of_rows = 8
    def number_of_columns = 5
    def highest_number_to_use_in_test = 5
    def number_of_tests_to_create = 2
    def type_of_test = "subtract" // one of add, subtract, multiply, divide

    // you don't need to change anything below this

    Random random = new Random()

    static void main(args) {
        MakeMathQuiz test = new MakeMathQuiz()
        if (args) {
            test.type_of_test = args[0]
        }

        test.createHtmlFile("math-test.html")
    }


    int getRandomNumberBetween(int minValue, int maxValue) {
        random.nextInt(maxValue) + minValue
    }

    String buildCell() {
        if (type_of_test == "add") return buildAdditionCell()
        if (type_of_test == "subtract") return buildSubtractionCell()
        if (type_of_test == "multiply") return buildMultiplicationCell()
        throw new RuntimeException("I don't know what type of cell to build.")
    }

    String buildAdditionCell() {
        """${getRandomNumberBetween(1, highest_number_to_use_in_test)} <br>
       + ${getRandomNumberBetween(1, highest_number_to_use_in_test)} <br>
      <hr width=40px>
    """
    }

    String buildSubtractionCell() {
        int topValue = getRandomNumberBetween(2, highest_number_to_use_in_test)
        """${topValue} <br>
           - ${getRandomNumberBetween(1, topValue - 1)} <br>
          <hr width=40px>
        """
    }

    String buildMultiplicationCell() {
        """${getRandomNumberBetween(1, highest_number_to_use_in_test)} <br>
       x ${getRandomNumberBetween(1, highest_number_to_use_in_test)} <br>
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

