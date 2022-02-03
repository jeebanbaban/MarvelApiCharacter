package marvel.api.marvelcharacters.ui.characterlist.callback

import marvel.api.marvelcharacters.data.model.Results

interface OnItemClickListener {
    fun onItemClick(result: Results)
}