package com.github.model4s.modelfree

import scala.annotation.StaticAnnotation

object Rest {
  class get extends StaticAnnotation
  class put extends StaticAnnotation
  class post extends StaticAnnotation
  class delete extends StaticAnnotation
}
