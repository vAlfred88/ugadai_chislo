function getCharacterName($request) {
    try {
        // Possible names: Сбер, Афина, Джой
        return $request.rawRequest.payload.character.name;
    } catch (e) {
        if ($request.channelType === "chatwidget") {
            return "Афина";
        }
        throw e.message;
    }
}
