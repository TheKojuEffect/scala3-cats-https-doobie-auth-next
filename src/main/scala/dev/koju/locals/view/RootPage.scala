package dev.koju.locals.view

import scalatags.Text.TypedTag
import scalatags.Text.all.{body, div, h1, head, html, id, p, _}
import scalatags.Text.attrs.charset
import scalatags.Text.tags.meta

object RootPage {
  def index: TypedTag[String] =
    html(
      head(
        scalatags.Text.tags2.title("NepaliUS"),
        meta(charset := "UTF-8"),
      ),
      body(
        div(
          h1(id := "title", "NepaliUS"),
          p("Nepali communities in US"),
        ),
      ),
    )
}
