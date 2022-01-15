package com.nepalius

import org.http4s.HttpRoutes

trait Controller[F[_]]:
  def routes: HttpRoutes[F]
