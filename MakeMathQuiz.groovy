#! /usr/bin/env groovy

import groovy.xml.MarkupBuilder

class MakeMathQuiz {

    // todo - don't page break the last table

    // editable values

    def number_of_rows = 7
    def number_of_columns = 10
    def highest_number_to_use_in_test = 18
    def maximum_addition_total = 20
    def number_of_tests_to_create = 5
    def type_of_test = "divide" // one of add, subtract, multiply, divide

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

    List getPrimeFactors(int target) {
        if (target == 1) return [1]
     
        if (target < 4) return [1, target]
     
        def targetSqrt = Math.sqrt(target)
        def lowfactors = (2..targetSqrt).grep { (target % it) == 0 }
        if (lowfactors == []) return [1, target]
        def nhalf = lowfactors.size() - ((lowfactors[-1] == targetSqrt) ? 1 : 0)
     
        [1] + lowfactors + (0..<nhalf).collect { target.intdiv(lowfactors[it]) }.reverse() + [target]
    }

    String buildCell() {
        if (type_of_test == "add") return buildAdditionCell()
        if (type_of_test == "subtract") return buildSubtractionCell()
        if (type_of_test == "multiply") return buildMultiplicationCell()
        if (type_of_test == "divide") return buildDivisionCell()
        throw new RuntimeException("I don't know what type of cell to build.")
    }

    String buildAdditionCell() {
        int topValue = getRandomNumberBetween(1, highest_number_to_use_in_test)
        """<span style="float: right">${topValue}</span><br>
       <span style="float: left">+</span><span style="float: right">${getRandomNumberBetween(1, maximum_addition_total - topValue)}</span><br>
      <hr width=40px style="margin-top: 4px">
    """
    }

    String buildSubtractionCell() {
        int topValue = getRandomNumberBetween(2, highest_number_to_use_in_test)
        """${topValue} <br>
           - ${getRandomNumberBetween(1, topValue - 1)} <br>
          <hr width=40px>
        """
    }

    String buildDivisionCell() {
        int numerator = (getRandomNumber(highest_number_to_use_in_test) + 1) * (getRandomNumber(highest_number_to_use_in_test) + 1)
        List factors = getPrimeFactors(numerator)
        // if we have more than 1 and the number, remove them to ensure a more interesting division problem
        if (factors.size() > 2) {
            factors.remove(factors.size() - 1)
            factors.remove(0)
        }
        Collections.shuffle(factors, new Random())
        int denominator = factors[0]
        
        """${numerator} <br>
           &divide; ${denominator} <br>
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
                                    td([style: 'font-size:20px; padding-left:30px; padding-right:30px;padding-bottom:120px'], { mkp.yieldUnescaped(buildCell()) })
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

