package com.daggery.nots.settings.theme.data

import com.daggery.nots.R
import com.daggery.nots.settings.theme.utils.TileThemeData

class TileThemeDataSource {

    companion object {
        val darkThemeTile = TileThemeData(
            title = "Default Dark",
            secondaryColorRes = R.color.default_dark_secondary,
            surfaceColorRes = R.color.default_dark_surface,
            onClickListener = null
        )

        val nordTile = TileThemeData(
            title = "Nord",
            secondaryColorRes = R.color.nord_secondary,
            surfaceColorRes = R.color.nord_surface,
            onClickListener = null
        )

        val steelBlueTile = TileThemeData(
            title = "Steel Blue",
            secondaryColorRes = R.color.steel_blue_secondary,
            surfaceColorRes = R.color.steel_blue_surface,
            onClickListener = null
        )

        val royalLavenderTile = TileThemeData(
            title = "Royal Lavender",
            secondaryColorRes = R.color.royal_lavender_secondary,
            surfaceColorRes = R.color.royal_lavender_surface,
            onClickListener = null
        )

        val heatherBerryTile = TileThemeData(
            title = "Heather Berry",
            secondaryColorRes = R.color.heather_berry_secondary,
            surfaceColorRes = R.color.heather_berry_surface,
            onClickListener = null
        )

    }

}